package fr.heriamc.bukkit.game.packet;

import fr.heriamc.api.messaging.packet.HeriaPacket;
import fr.heriamc.api.messaging.packet.HeriaPacketChannel;

import java.util.UUID;

public class GameJoinPacket extends HeriaPacket {

    private final UUID uuid;
    private final String gameName;

    public GameJoinPacket(UUID uuid, String gameName) {
        super(HeriaPacketChannel.GAME);
        this.uuid = uuid;
        this.gameName = gameName;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getGameName() {
        return gameName;
    }

}