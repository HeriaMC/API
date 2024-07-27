package fr.heriamc.api.server;

import fr.heriamc.api.data.CacheDataManager;
import fr.heriamc.api.data.redis.RedisConnection;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HeriaServerManager extends CacheDataManager<String, HeriaServer> {
    public HeriaServerManager(RedisConnection redisConnection) {
        super(redisConnection, "servers");
    }

    public List<HeriaServer> getAll(HeriaServerType type){
        return this.getAllInCache().stream()
                .filter(starServer -> starServer.getType() == type)
                .collect(Collectors.toList());
    }

    public HeriaServer getWithLessPlayers(HeriaServerType type){
        return this.getAll(type).stream()
                .min(Comparator.comparingInt(HeriaServer::getConnectedCount))
                .orElse(null);
    }
}
