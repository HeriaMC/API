package fr.heriamc.proxy.packet;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import fr.heriamc.api.messaging.packet.HeriaPacket;
import fr.heriamc.api.messaging.packet.HeriaPacketReceiver;
import fr.heriamc.proxy.HeriaProxy;

import java.net.InetSocketAddress;

public class ProxyPacketReceiver implements HeriaPacketReceiver {

    private final HeriaProxy proxy;

    public ProxyPacketReceiver(HeriaProxy heriaProxy) {
        this.proxy = heriaProxy;
    }

    @Override
    public void execute(String channel, HeriaPacket packet) {
        if(packet instanceof SendPlayerPacket found){
            Player player = proxy.getServer().getPlayer(found.getPlayerUUID()).orElse(null);
            RegisteredServer server = proxy.getServer().getServer(found.getServerTarget()).orElse(null);

            if(server == null || player == null){
                return;
            }

            player.createConnectionRequest(server);
        }

        if(packet instanceof ServerRegisterPacket found){
            ServerInfo serverInfo = new ServerInfo(found.getServerName(), new InetSocketAddress(found.getServerPort()));
            this.proxy.getServer().registerServer(serverInfo);
        }
    }

}
