package fr.heriamc.bukkit.chat.command;

import fr.heriamc.api.user.HeriaPlayer;
import fr.heriamc.api.user.rank.HeriaRank;
import fr.heriamc.bukkit.HeriaBukkit;
import fr.heriamc.bukkit.chat.HeriaChatMessage;
import fr.heriamc.bukkit.command.CommandArgs;
import fr.heriamc.bukkit.command.HeriaCommand;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ReportChatCommand {

    private final HeriaBukkit bukkit;

    public ReportChatCommand(HeriaBukkit bukkit) {
        this.bukkit = bukkit;
    }

    @HeriaCommand(name = "reportchat", inGameOnly = true, power = HeriaRank.PLAYER)
    public void onCommand(CommandArgs args){
        Player player = args.getPlayer();

        if(args.getArgs().length < 1){
            player.sendMessage("§c/reportchat <id>");
            return;
        }

        UUID id;
        try {
            id = UUID.fromString(args.getArgs(0));
        } catch (Exception e){
            player.sendMessage("§cID non valide.");
            return;
        }

        HeriaChatMessage chatMessage = this.bukkit.getChatManager().get(id);

        if(chatMessage == null){
            player.sendMessage("§cCe message n'existe pas ou plus.");
            return;
        }

        if(chatMessage.getSender().equals(args.getPlayer().getUniqueId())){
            args.getPlayer().sendMessage("§cVous ne pouvez pas report votre propre message.");
            return;
        }

        if(chatMessage.isReported()){
            player.sendMessage("§cCe message est déjà report.");
            return;
        }

        if(args.getArgs().length == 2 && args.getArgs(1).equals("confirm")){
            chatMessage.setReported(true);
            bukkit.getChatManager().save(chatMessage);

            //TODO: create report


            player.sendMessage("§aVotre report a bien été pris en compte.");
            return;
        }

        HeriaPlayer reported = this.bukkit.getApi().getPlayerManager().get(chatMessage.getSender());

        TextComponent confirm = new TextComponent(TextComponent.fromLegacyText("§a[§a§l✓ §aConfirmer]"));
        confirm.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§aÊtes vous sur de vouloir signaler le message de " + reported.getName() + " ?")));
        confirm.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/reportchat " + id + " confirm"));

        player.sendMessage("§fVoulez-vous vraiment signaler le message de §6" + reported.getName() + " §7?");
        player.sendMessage("");
        player.sendMessage(" §7\"§7§o" + chatMessage.getContent() + "§7\"");
        player.sendMessage("");
        player.spigot().sendMessage(confirm);
    }
}