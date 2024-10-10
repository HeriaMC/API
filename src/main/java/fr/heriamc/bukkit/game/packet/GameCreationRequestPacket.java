package fr.heriamc.bukkit.game.packet;

import fr.heriamc.api.messaging.packet.HeriaPacket;
import fr.heriamc.api.messaging.packet.HeriaPacketChannel;

import java.util.UUID;

public class GameCreationRequestPacket extends HeriaPacket {

    private final UUID requestID;
    private final String server;

    public GameCreationRequestPacket(UUID requestID, String server) {
        super(HeriaPacketChannel.GAME);
        this.requestID = requestID;
        this.server = server;
    }

    public UUID getRequestID() {
        return requestID;
    }

    public String getServer() {
        return server;
    }
}
