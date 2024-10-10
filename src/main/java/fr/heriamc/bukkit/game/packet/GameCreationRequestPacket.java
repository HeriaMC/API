package fr.heriamc.bukkit.game.packet;

import fr.heriamc.api.messaging.packet.HeriaPacket;
import fr.heriamc.api.messaging.packet.HeriaPacketChannel;
import fr.heriamc.api.server.HeriaServerType;

import java.util.UUID;

public class GameCreationRequestPacket extends HeriaPacket {

    private final UUID requestID;
    private final String server;
    private final HeriaServerType serverType;

    public GameCreationRequestPacket(UUID requestID, String server, HeriaServerType serverType) {
        super(HeriaPacketChannel.GAME);
        this.requestID = requestID;
        this.server = server;
        this.serverType = serverType;
    }

    public UUID getRequestID() {
        return requestID;
    }

    public String getServer() {
        return server;
    }

    public HeriaServerType getServerType() {
        return serverType;
    }
}
