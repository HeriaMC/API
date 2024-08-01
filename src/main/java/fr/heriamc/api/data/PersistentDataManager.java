package fr.heriamc.api.data;

import com.mongodb.client.model.Filters;
import fr.heriamc.api.data.mongo.MongoConnection;
import fr.heriamc.api.data.redis.RedisConnection;
import fr.heriamc.api.data.resolver.DataResolver;
import fr.heriamc.api.data.resolver.Defaultable;
import fr.heriamc.api.utils.AnnotatedFieldRetriever;
import org.bson.Document;
import redis.clients.jedis.Jedis;

public abstract class PersistentDataManager<A, D extends SerializableData<A>> extends CacheDataManager<A, D>
        implements Defaultable<D>, AnnotatedFieldRetriever {

    protected final MongoConnection mongoConnection;
    protected final String mongoCollection;

    public PersistentDataManager(RedisConnection redisConnection, MongoConnection mongoConnection,
                                 String redisKey, String mongoCollection) {

        super(redisConnection, redisKey);
        this.mongoConnection = mongoConnection;
        this.mongoCollection = mongoCollection;
    }

    @Override
    public D get(A identifier){
        D data = this.getInLocal(identifier);

        if(data == null){
            data = this.getInCache(identifier);

            boolean toCache = false;
            if(data == null){
                data = this.loadInPersistant(identifier);
                toCache = true;
            }

            if(data != null){
                if(toCache){
                    this.putInCache(data);
                }

                this.putInLocal(data);
            }
        }

        return data;
    }

    public D loadInPersistant(A identifier){
        Document document = this.mongoConnection.getDatabase().getCollection(this.mongoCollection)
                .find(Filters.eq("id", identifier.toString())).first();

        if(document == null){
            return null;
        }

        Document checked = DataResolver.resolveJson(this, document);
        D data = SerializableData.fromJson(checked.toJson(), this.getClazz(1));

        try (Jedis jedis = this.redisConnection.getResource()) {
            jedis.hset(this.redisKey, identifier.toString(), data.toJson());
        }

        return data;
    }

    public void saveInPersistant(D data){
        Document document = Document.parse(data.toJson());

        for (String annotated : this.getAnnotatedFields(this.getClazz(1), NonPersistantData.class)) {
            document.remove(annotated);
        }

        this.mongoConnection.getDatabase().getCollection(this.mongoCollection)
                .replaceOne(new Document("id", data.getIdentifier().toString()), document);
    }

    public D createOrLoad(A identifier){
        D data = this.get(identifier);

        if(data != null){
            this.putInCache(data);
            return data;
        }

        D newData = SerializableData.fromJson(this.getDefault().toJson(), this.getClazz(1));
        newData.setIdentifier(identifier);
        Document document = Document.parse(newData.toJson());

        this.mongoConnection.getDatabase().getCollection(this.mongoCollection).insertOne(document);
        this.putInCache(newData);

        return newData;
    }

}
