package fr.heriamc.proxy.pool;

import fr.heriamc.api.game.size.GameSize;
import fr.heriamc.api.messaging.packet.HeriaPacket;
import fr.heriamc.api.server.HeriaServerType;

import java.util.List;
import java.util.UUID;

public interface HeriaPool {

    boolean isAvailable();
    List<HeriaPacket> createPackets(UUID players);

    HeriaServerType getServerType();

    GameSize getGameSize();

}
