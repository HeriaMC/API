package fr.heriamc.bukkit.group;

import fr.heriamc.api.data.CacheDataManager;
import fr.heriamc.api.data.redis.RedisConnection;

import java.util.UUID;

public class PlayerGroupManager extends CacheDataManager<UUID, PlayerGroup> {
    public PlayerGroupManager(RedisConnection redisConnection) {
        super(redisConnection, "playerGroups");
    }

}
