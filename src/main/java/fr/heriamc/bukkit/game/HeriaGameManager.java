package fr.heriamc.bukkit.game;

import fr.heriamc.api.data.CacheDataManager;
import fr.heriamc.api.data.redis.RedisConnection;

public class HeriaGameManager extends CacheDataManager<String, HeriaGamesList> {

    public HeriaGameManager(RedisConnection redisConnection) {
        super(redisConnection, "games");
    }

}
