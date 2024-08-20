package fr.heriamc.bukkit.chat;

import fr.heriamc.api.user.HeriaPlayer;
import fr.heriamc.bukkit.HeriaBukkit;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

public class HeriaChatListener implements Listener {

    private final HeriaBukkit bukkit;

    public HeriaChatListener(HeriaBukkit bukkit) {
        this.bukkit = bukkit;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent e){
        Player player = e.getPlayer();
        UUID chatID = UUID.randomUUID();

        HeriaChatMessage chat = new HeriaChatMessage(chatID, player.getUniqueId(), e.getMessage(), false);
        bukkit.getChatManager().put(chat);

        if(e.isCancelled()){
            return;
        }

        HeriaPlayer heriaPlayer = bukkit.getApi().getPlayerManager().get(player.getUniqueId());

        TextComponent reportSymbol = new TextComponent(TextComponent.fromLegacyText("⚠"));
        reportSymbol.setColor(ChatColor.RED);
        reportSymbol.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/reportchat " + chatID));
        reportSymbol.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder("Cliquez pour signaler " + player.getName()).color(ChatColor.RED).create()));

        TextComponent message = new TextComponent(TextComponent.fromLegacyText(" " + heriaPlayer.getRank().getPrefix() + player.getName() +" §8» §f" + e.getMessage()));

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.spigot().sendMessage(reportSymbol, message);
        }

        e.setCancelled(true);
    }
}
