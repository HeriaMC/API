package fr.heriamc.proxy.pool.types;

import fr.heriamc.api.game.size.GameSize;
import fr.heriamc.api.messaging.packet.HeriaPacket;
import fr.heriamc.api.server.HeriaServer;
import fr.heriamc.api.server.HeriaServerType;
import fr.heriamc.proxy.HeriaProxy;
import fr.heriamc.proxy.pool.HeriaPool;
import fr.heriamc.proxy.queue.HeriaQueueHandler;
import fr.heriamc.proxy.utils.ProxyPacketUtil;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ServerPool implements HeriaPool {

    protected final HeriaProxy proxy;
    protected final HeriaServerType serverType;

    protected String lastServer;
    protected boolean isServerEnabled;

    public ServerPool(HeriaProxy proxy, HeriaServerType serverType) {
        this.proxy = proxy;
        this.serverType = serverType;

        createServer();
    }

    public void createServer(){
        this.lastServer = proxy.getApi().getServerCreator().createServer(serverType, null);
        this.isServerEnabled = false;

        proxy.getServer().getScheduler().buildTask(proxy, (scheduledTask) -> {
            HeriaServer server = this.proxy.getApi().getServerManager().get(lastServer);

            if(server == null){
                return;
            }

            if(server.getStatus().isReachable()){
                this.isServerEnabled = true;
                scheduledTask.cancel();
            }
        }).repeat(1, TimeUnit.SECONDS).schedule();
    }

    @Override
    public boolean isAvailable() {
        return lastServer != null && isServerEnabled;
    }

    @Override
    public List<HeriaPacket> createPackets(UUID player) {
        List<HeriaPacket> packets = ProxyPacketUtil.buildJoinServer(player, lastServer);

        if(!isOldCorrect()){
            lastServer = null;
        }

        return packets;
    }

    private boolean isOldCorrect(){
        HeriaServer server = proxy.getApi().getServerManager().get(this.lastServer);

        if(server.getConnected().size() >= HeriaQueueHandler.MAX_SERVER_SIZE){
            return false;
        }

        createServer();

        return true;
    }

    @Override
    public HeriaServerType getServerType() {
        return serverType;
    }

    @Override
    public GameSize getGameSize() {
        return null;
    }
}
