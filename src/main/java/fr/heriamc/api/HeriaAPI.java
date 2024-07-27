package fr.heriamc.api;

import fr.heriamc.api.data.mongo.MongoConnection;
import fr.heriamc.api.data.redis.RedisConnection;
import fr.heriamc.api.messaging.HeriaMessaging;
import fr.heriamc.api.server.HeriaServerManager;
import fr.heriamc.api.server.creator.HeriaServerCreator;
import fr.heriamc.api.user.HeriaPlayerManager;
import redis.clients.jedis.JedisPool;

public class HeriaAPI {

    private static HeriaAPI instance;

    private final HeriaConfiguration configuration;

    private final MongoConnection mongoConnection;
    private final RedisConnection redisConnection;

    private final HeriaMessaging heriaMessaging;
    private final HeriaPlayerManager playerManager;
    private final HeriaServerManager serverManager;
    private final HeriaServerCreator serverCreator;

    public HeriaAPI(HeriaConfiguration configuration) {
        instance = this;

        this.configuration = configuration;
        this.mongoConnection = new MongoConnection(configuration.getMongoCredentials());
        this.redisConnection = new RedisConnection(configuration.getRedisCredentials(), 0);

        this.heriaMessaging = new HeriaMessaging(this.redisConnection);
        this.playerManager = new HeriaPlayerManager(this.redisConnection, this.mongoConnection);
        this.serverManager = new HeriaServerManager(this.redisConnection);
        this.serverCreator = new HeriaServerCreator(this.serverManager);
    }

    public void onDisable(){
        this.heriaMessaging.stop();

        JedisPool pool = this.redisConnection.getPool();
        pool.close();
        pool.destroy();
    }

    public static HeriaAPI createHeriaAPI(HeriaConfiguration configuration){
        return new HeriaAPI(configuration);
    }

    public static HeriaAPI get() {
        return instance;
    }

    public HeriaConfiguration getConfiguration() {
        return configuration;
    }

    public MongoConnection getMongoConnection() {
        return mongoConnection;
    }

    public RedisConnection getRedisConnection() {
        return redisConnection;
    }

    public HeriaMessaging getMessaging() {
        return heriaMessaging;
    }

    public HeriaPlayerManager getPlayerManager() {
        return playerManager;
    }

    public HeriaServerManager getServerManager() {
        return serverManager;
    }

    public HeriaServerCreator getServerCreator() {
        return serverCreator;
    }
}