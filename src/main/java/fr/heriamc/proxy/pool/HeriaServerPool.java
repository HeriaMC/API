package fr.heriamc.proxy.pool;

import fr.heriamc.api.server.HeriaServer;
import fr.heriamc.api.server.HeriaServerType;
import fr.heriamc.bukkit.game.size.GameSize;
import fr.heriamc.proxy.HeriaProxy;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class HeriaServerPool {

    private final HeriaProxy proxy;
    private final HeriaServerType serverType;

    private String lastServer;
    private boolean isLastServerEnabled;

    public HeriaServerPool(HeriaProxy proxy, HeriaServerType serverType) {
        this.proxy = proxy;
        this.serverType = serverType;
    }

    public void createGames(List<GameSize> sizes){

    }

    public void createGame(){

    }

    public void createServer(){
        this.lastServer = proxy.getApi().getServerCreator().createServer(serverType, null);
        this.isLastServerEnabled = false;

        proxy.getServer().getScheduler().buildTask(proxy, (scheduledTask) -> {
            HeriaServer server = this.proxy.getApi().getServerManager().get(lastServer);

            if(server == null){
                return;
            }

            if(server.getStatus().isReachable()){
                scheduledTask.cancel();
            }
        }).repeat(1, TimeUnit.SECONDS).schedule();
    }
}
