package fr.heriamc.api.user;

import fr.heriamc.api.data.PersistentDataManager;
import fr.heriamc.api.data.mongo.MongoConnection;
import fr.heriamc.api.data.redis.RedisConnection;
import fr.heriamc.api.user.rank.HeriaRank;

import java.util.ArrayList;
import java.util.UUID;

public class HeriaPlayerManager extends PersistentDataManager<UUID, HeriaPlayer> {

    public HeriaPlayerManager(RedisConnection redisConnection, MongoConnection mongoConnection) {
        super(redisConnection, mongoConnection, "players", "accounts");
    }

    @Override
    public HeriaPlayer getDefault() {
        return new HeriaPlayer(null, null, HeriaRank.DEFAULT, null, null,
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 0, 0,0);
    }

}
