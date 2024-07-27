package fr.starblade.api.data;

import fr.starblade.api.data.redis.RedisConnection;
import fr.starblade.api.utils.ClassProvider;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.stream.Collectors;

public abstract class CacheDataManager<A, D extends SerializableData<A>> extends LocalDataManager<A, D>
        implements ClassProvider<D> {

    protected final RedisConnection redisConnection;
    protected final String redisKey;

    public CacheDataManager(RedisConnection redisConnection, String redisKey) {
        this.redisConnection = redisConnection;
        this.redisKey = redisKey;
    }

    public CacheDataManager(RedisConnection redisConnection, LocalDataExpiration expiration, String redisKey) {
        super(expiration);
        this.redisConnection = redisConnection;
        this.redisKey = redisKey;
    }


    public D get(A identifier){
        D data = this.getInLocal(identifier);

        if(data == null){
            data = this.getInCache(identifier);

            if(data != null){
                this.putInLocal(data);
            }
        }

        return data;
    }

    public void put(D data){
        this.save(data);
    }

    public void save(D data){
        this.putInCache(data);
        this.putInLocal(data);
    }

    public void remove(A identifier){
        this.removeInLocal(identifier);
        this.removeInCache(identifier);
    }

    public D getInCache(A identifier){
        try (Jedis jedis = this.redisConnection.getResource()) {
            String redisData = jedis.hget(this.redisKey, identifier.toString());
            if(redisData == null) return null;
            return SerializableData.fromJson(redisData, this.getClazz(1));
        }
    }

    public void removeInCache(A identifier){
        try (Jedis jedis = this.redisConnection.getResource()) {
            jedis.hdel(this.redisKey, identifier.toString());
        }
        this.removeInLocal(identifier);
    }

    public void putInCache(D data){
        try (Jedis jedis = this.redisConnection.getResource()) {
            jedis.hset(this.redisKey, data.getIdentifier().toString(), data.toJson());
        }
    }

    public List<D> getAllInCache(){
        try (Jedis jedis = this.redisConnection.getResource()) {
            return jedis.hgetAll(this.redisKey).values()
                    .stream()
                    .map(rd -> SerializableData.fromJson(rd, this.getClazz(1)))
                    .collect(Collectors.toList());
        }
    }

}
