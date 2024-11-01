package fr.heriamc.bukkit.queue;

import fr.heriamc.api.user.HeriaPlayer;
import fr.heriamc.bukkit.HeriaBukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class BukkitQueueManager implements Listener {

    private final HeriaBukkit bukkit;

    public BukkitQueueManager(HeriaBukkit bukkit) {
        this.bukkit = bukkit;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        HeriaPlayer heriaPlayer = bukkit.getApi().getPlayerManager().get(player.getUniqueId());

        if(heriaPlayer.isInQueue()){
            showQueue(player);
        }
    }

    public void showQueue(Player player){
        bukkit.getServer().getScheduler().runTaskTimer(bukkit, new QueueTask(bukkit, player), 0L, 10L);
    }
}
