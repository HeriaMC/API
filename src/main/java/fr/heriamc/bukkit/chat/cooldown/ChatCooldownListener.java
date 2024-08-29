package fr.heriamc.bukkit.chat.cooldown;

import fr.heriamc.bukkit.chat.HeriaChatManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class ChatCooldownListener implements Listener {

    private final HeriaChatManager chatManager;

    public ChatCooldownListener(HeriaChatManager chatManager) {
        this.chatManager = chatManager;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();
        ChatCooldownManager cooldownManager = chatManager.getCooldownManager();

        if(cooldownManager.getInLocal(player) != null){
            player.sendMessage("§cVeuillez ne pas parler trop rapidement dans le chat");
            event.setCancelled(true);
            return;
        }

        cooldownManager.putInLocal(player, true);
    }
}