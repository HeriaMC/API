package fr.heriamc.proxy.pool.types;

import fr.heriamc.api.messaging.packet.HeriaPacket;
import fr.heriamc.api.server.HeriaServer;
import fr.heriamc.api.server.HeriaServerType;
import fr.heriamc.proxy.HeriaProxy;
import fr.heriamc.proxy.packet.SendPlayerPacket;
import fr.heriamc.proxy.pool.HeriaPool;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ServerPool implements HeriaPool {

    protected final HeriaProxy proxy;
    protected final HeriaServerType serverType;

    protected String lastServer;
    protected boolean isServerEnabled;

    private int serversToCreate;

    public ServerPool(HeriaProxy proxy, HeriaServerType serverType) {
        this.proxy = proxy;
        this.serverType = serverType;

        run();
    }

    @Override
    public void run() {
        proxy.getServer().getScheduler().buildTask(proxy, (scheduledTask -> {
            if(!isServerEnabled){
                return;
            }

        })).repeat(500, TimeUnit.MILLISECONDS).schedule();
    }

    @Override
    public void createForPlayers(int players) {

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
        List<HeriaPacket> packets = new ArrayList<>();
        packets.add(new SendPlayerPacket(player, lastServer));

        if(!isOldCorrect()){
            lastServer = null;
        }

        return packets;
    }

    private boolean isOldCorrect(){
        return false;
    }
}
