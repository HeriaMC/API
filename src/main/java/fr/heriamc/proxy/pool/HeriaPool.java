package fr.heriamc.proxy.pool;

import fr.heriamc.api.messaging.packet.HeriaPacket;

import java.util.List;
import java.util.UUID;

public interface HeriaPool {

    void run();
    void createForPlayers(int players);

    boolean isAvailable();
    List<HeriaPacket> createPackets(UUID players);


}
