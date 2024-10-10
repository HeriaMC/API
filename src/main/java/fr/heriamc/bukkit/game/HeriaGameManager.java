package fr.heriamc.bukkit.game;

import fr.heriamc.api.data.CacheDataManager;
import fr.heriamc.api.data.redis.RedisConnection;
import fr.heriamc.api.server.HeriaServerType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HeriaGameManager extends CacheDataManager<String, HeriaGamesList> {

    public HeriaGameManager(RedisConnection redisConnection) {
        super(redisConnection, "games");
    }

    public String getServerName(HeriaGameInfo gameInfo){
        return "";
    }

    public List<HeriaGameInfo> getGames(String serverName){
        return this.get(serverName).getGames();
    }

    public List<HeriaGameInfo> getGames(HeriaServerType serverType){
        List<HeriaGameInfo> games = new ArrayList<>();
        for (HeriaGamesList game : this.getAllInCache()) {
            if(game.getServerType() == serverType){
                games.addAll(game.getGames());
            }
        }
        return games;
    }



}
