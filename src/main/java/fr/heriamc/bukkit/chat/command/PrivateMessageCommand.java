package fr.heriamc.bukkit.chat.command;

import fr.heriamc.api.user.HeriaPlayer;
import fr.heriamc.api.user.rank.HeriaRank;
import fr.heriamc.api.user.resolver.HeriaPlayerResolver;
import fr.heriamc.bukkit.HeriaBukkit;
import fr.heriamc.bukkit.command.CommandArgs;
import fr.heriamc.bukkit.command.HeriaCommand;
import fr.heriamc.proxy.packet.SendPlayerMessagePacket;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.UUID;

public class PrivateMessageCommand {

    private final HeriaBukkit bukkit;

    public PrivateMessageCommand(HeriaBukkit bukkit) {
        this.bukkit = bukkit;
    }

    @HeriaCommand(name = "message", inGameOnly = true, power = HeriaRank.PLAYER, aliases = {"msg", "tell"})
    public void onMessageCommand(CommandArgs args){
        Player player = args.getPlayer();
        HeriaPlayer heriaPlayer = bukkit.getApi().getPlayerManager().get(player.getUniqueId());

        if(args.getArgs().length < 2){
            player.sendMessage("§c/message <joueur> <message>");
            return;
        }

        String targetName = args.getArgs(0);
        HeriaPlayerResolver resolver = bukkit.getApi().getResolverManager().get(targetName);

        if(resolver == null){
            player.sendMessage("§cCe joueur ne s'est jamais connecté.");
            return;
        }


        HeriaPlayer target = bukkit.getApi().getPlayerManager().get(resolver.getUuid());

        if(target == null){
            player.sendMessage("§cCe joueur n'existe plus, il a probablement changé de pseudonyme.");
            return;
        }

        if(!target.isConnected()){
            player.sendMessage("§cCe joueur n'est pas connecté.");
            return;
        }

        String message = String.join(" ", Arrays.copyOfRange(args.getArgs(), 1, args.getArgs().length));

        player.sendMessage("§fMessage envoyé à §6" + target.getName() + "§f: " + message);
        bukkit.getApi().getMessaging().send(new SendPlayerMessagePacket(target.getId(), "§fMessage reçu de §6" + heriaPlayer.getName() + "§f: " + message));

        heriaPlayer.setReply(target.getId());
        bukkit.getApi().getPlayerManager().save(heriaPlayer);
        target.setReply(heriaPlayer.getId());
        bukkit.getApi().getPlayerManager().save(target);
    }

    @HeriaCommand(name = "reply", inGameOnly = true, power = HeriaRank.PLAYER, aliases = {"r"})
    public void onReplyCommand(CommandArgs args){
        Player player = args.getPlayer();
        HeriaPlayer heriaPlayer = bukkit.getApi().getPlayerManager().get(player.getUniqueId());

        if(args.getArgs().length < 1){
            player.sendMessage("/reply <message>");
            return;
        }

        UUID reply = heriaPlayer.getReply();
        if(reply == null){
            player.sendMessage("§cVous n'avez personne à qui répondre");
            return;
        }

        HeriaPlayer heriaReply = bukkit.getApi().getPlayerManager().get(reply);

        if(heriaReply == null || !heriaReply.isConnected()){
            player.sendMessage("§cLe joueur à qui vous voulez répondre n'est plus conencté");
            return;
        }

        String message = String.join(" ", Arrays.copyOfRange(args.getArgs(), 0, args.getArgs().length));

        player.sendMessage("§fMessage envoyé à §6" + heriaReply.getName() + "§f: " + message);
        bukkit.getApi().getMessaging().send(new SendPlayerMessagePacket(heriaReply.getId(), "§fMessage reçu de §6" + heriaPlayer.getName() + "§f: " + message));
    }
}
