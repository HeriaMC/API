package fr.heriamc.bukkit.chat;

import fr.heriamc.api.sanction.HeriaSanction;
import fr.heriamc.api.sanction.HeriaSanctionType;
import fr.heriamc.api.user.HeriaPlayer;
import fr.heriamc.api.user.rank.HeriaRank;
import fr.heriamc.bukkit.HeriaBukkit;
import fr.heriamc.bukkit.chat.event.HeriaChatEvent;
import fr.heriamc.bukkit.utils.TimeUtils;
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

import java.util.List;
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

        List<HeriaSanction> mutes = bukkit.getApi().getSanctionManager().getActiveSanctions(heriaPlayer.getId(), HeriaSanctionType.MUTE);

        if(!mutes.isEmpty()){
            HeriaSanction mute = mutes.get(0);

            for (String s : bukkit.getApi().getSanctionManager().getMuteMessage(mute)) {
                player.sendMessage(s);
            }

            e.setCancelled(true);
            return;
        }


        TextComponent reportSymbol = new TextComponent(TextComponent.fromLegacyText("⚠"));
        reportSymbol.setColor(ChatColor.RED);
        reportSymbol.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/reportchat " + chatID));
        reportSymbol.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder("Cliquez pour signaler " + player.getName()).color(ChatColor.RED).create()));

        HeriaRank displayed = null;
        if(heriaPlayer.isRemovedTag()){
            displayed = HeriaRank.DEFAULT;
        } else if(heriaPlayer.isNicked()){
            HeriaRank newRank = heriaPlayer.getNickData().getNewRank();
            if(newRank != null){
                displayed = newRank;
            }
        }

        if(displayed == null){
            displayed = heriaPlayer.getRank();
        }

        TextComponent message = new TextComponent(TextComponent.fromLegacyText(" " + displayed.getPrefix() + heriaPlayer.getNickedName() +" §8» §f" + e.getMessage()));

        HeriaChatEvent chatEvent = new HeriaChatEvent(player, heriaPlayer, heriaPlayer.getNickedName(), reportSymbol, message);
        bukkit.getServer().getPluginManager().callEvent(chatEvent);

        if(chatEvent.isCancelled()){
            e.setCancelled(true);
            return;
        }

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.spigot().sendMessage(reportSymbol, message);
        }

        e.setCancelled(true);
    }
}
