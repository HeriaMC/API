package fr.heriamc.bukkit.queue;

import fr.heriamc.api.queue.HeriaQueue;
import fr.heriamc.api.user.HeriaPlayer;
import fr.heriamc.bukkit.HeriaBukkit;
import fr.heriamc.bukkit.utils.Title;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class QueueTask extends BukkitRunnable {

    private final HeriaBukkit bukkit;
    private final Player player;
    private final long joined;

    private UUID queueID;
    private HeriaQueue heriaQueue;

    public QueueTask(HeriaBukkit bukkit, Player player) {
        this.bukkit = bukkit;
        this.player = player;
        this.queueID = null;

        this.joined = bukkit.getApi().getPlayerManager().get(player.getUniqueId()).getJoinQueueTime();
    }

    @Override
    public void run() {
        if(queueID == null){
            HeriaPlayer heriaPlayer = bukkit.getApi().getPlayerManager().get(player.getUniqueId());
            UUID queue = heriaPlayer.getQueue();
            if(queue != null){
                this.queueID = queue;
            }
            return;
        }

        this.updateQueue();

        if(player == null || !player.isOnline()){
            this.cancel();
            return;
        }

        if(!heriaQueue.getPlayers().contains(player.getUniqueId())){
            this.cancel();
            return;
        }

        String serverName = heriaQueue.getServer();
        if(serverName == null){
            serverName = heriaQueue.getServerType().getName();
        }

        String gameSize = " " + heriaQueue.getGameSize().getName();

        Title.sendActionBar(player, "§7File d'attente vers: §6" + serverName + gameSize +
                "§8▏ §7Position: §6" + heriaQueue.getPlayerPosition(player.getUniqueId()) + "§8/§6" + heriaQueue.getPlayers().size()
                + " §8▏ §7Durée: §6" + joined);
    }

    public void updateQueue(){
        this.heriaQueue = bukkit.getApi().getQueueManager().get(queueID);
    }
}
