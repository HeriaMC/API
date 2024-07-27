package fr.heriamc.api.messaging.packet;

public interface StarPacketReceiver {

    void execute(String channel, HeriaPacket packet);

}
