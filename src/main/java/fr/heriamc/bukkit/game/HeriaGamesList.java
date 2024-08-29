package fr.heriamc.bukkit.game;

import fr.heriamc.api.data.SerializableData;
import fr.heriamc.api.server.HeriaServerType;

import java.util.List;

public class HeriaGamesList implements SerializableData<String>{

    private HeriaServerType serverType;
    private List<HeriaGameInfo> games;

    public HeriaGamesList(HeriaServerType serverType, List<HeriaGameInfo> games) {
        this.serverType = serverType;
        this.games = games;
    }

    public HeriaServerType getServerType() {
        return serverType;
    }

    public HeriaGamesList setServerType(HeriaServerType serverType) {
        this.serverType = serverType;
        return this;
    }

    public List<HeriaGameInfo> getGames() {
        return games;
    }

    public HeriaGamesList setGames(List<HeriaGameInfo> games) {
        this.games = games;
        return this;
    }

    @Override
    public String getIdentifier() {
        return this.serverType.getName();
    }

    @Override
    public void setIdentifier(String identifier) {
        // nothing
    }

}
