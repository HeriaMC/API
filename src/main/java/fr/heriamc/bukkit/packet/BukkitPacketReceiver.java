package fr.heriamc.bukkit.packet;

import fr.heriamc.api.messaging.packet.HeriaPacket;
import fr.heriamc.api.messaging.packet.HeriaPacketReceiver;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BukkitPacketReceiver implements HeriaPacketReceiver {

    @Override
    public void execute(String channel, HeriaPacket packet) {

        if(packet instanceof BukkitPlayerMessagePacket found){
            Player player = Bukkit.getPlayer(found.getPlayer());
            String message = found.getMessage();

            if(player == null || message == null){
                return;
            }


            try {
                System.out.println("message: " + message);
                BaseComponent[] components = ComponentSerializer.parse(message);
                for (BaseComponent component : components) {
                    ClickEvent clickEvent = component.getClickEvent();
                    System.out.println("clickEvent: " + clickEvent);
                }
                if(components == null){
                    throw new NullPointerException("components null");
                }
                player.spigot().sendMessage(components);
            } catch (Exception e){
                player.sendMessage(message);
            }

        }
    }

}
