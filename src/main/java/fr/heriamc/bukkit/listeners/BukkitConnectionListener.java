package fr.heriamc.bukkit.listeners;

import fr.heriamc.api.server.HeriaServer;
import fr.heriamc.api.user.HeriaPlayer;
import fr.heriamc.bukkit.HeriaBukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class BukkitConnectionListener implements Listener {

    private final HeriaBukkit bukkit;


    public BukkitConnectionListener(HeriaBukkit heriaBukkit) {
        this.bukkit = heriaBukkit;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        HeriaPlayer heriaPlayer = bukkit.getApi().getPlayerManager().get(player.getUniqueId());
        HeriaServer heriaServer = bukkit.getApi().getServerManager().get(bukkit.getInstanceName());

        heriaPlayer.setConnectedTo(heriaServer.getName());
        heriaServer.getConnected().add(heriaPlayer.getId());

        bukkit.getApi().getPlayerManager().save(heriaPlayer);
        bukkit.getApi().getServerManager().save(heriaServer);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();

        HeriaPlayer heriaPlayer = bukkit.getApi().getPlayerManager().get(player.getUniqueId());
        HeriaServer heriaServer = bukkit.getApi().getServerManager().get(bukkit.getInstanceName());

        heriaPlayer.setConnectedTo(null);
        heriaServer.getConnected().remove(heriaPlayer.getId());

        bukkit.getApi().getPlayerManager().save(heriaPlayer);
        bukkit.getApi().getServerManager().save(heriaServer);
    }
}
