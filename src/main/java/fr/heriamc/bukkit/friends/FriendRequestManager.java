package fr.heriamc.bukkit.friends;

import fr.heriamc.api.data.PersistentDataManager;
import fr.heriamc.api.data.mongo.MongoConnection;
import fr.heriamc.api.data.redis.RedisConnection;

import java.util.UUID;

public class FriendRequestManager extends PersistentDataManager<UUID, FriendRequest> {
    public FriendRequestManager(RedisConnection redisConnection, MongoConnection mongoConnection) {
        super(redisConnection, mongoConnection, "friendsrequests", "friendsrequests");
    }

    @Override
    public FriendRequest getDefault() {
        return new FriendRequest(null, null, null, System.currentTimeMillis());
    }
}
