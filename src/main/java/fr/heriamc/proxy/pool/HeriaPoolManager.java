package fr.heriamc.proxy.pool;

import fr.heriamc.api.server.HeriaServerType;
import fr.heriamc.api.game.size.GameSize;
import fr.heriamc.proxy.HeriaProxy;

import java.util.ArrayList;
import java.util.List;

public class HeriaPoolManager {

    private final HeriaProxy proxy;
    private final List<HeriaPool> pools = new ArrayList<>();

    public HeriaPoolManager(HeriaProxy proxy) {
        this.proxy = proxy;
    }

    public HeriaPool getServerPool(HeriaServerType type){


        return null;
    }

    public HeriaPool getGamePool(HeriaServerType type, GameSize gameSize){

        return null;
    }


}
