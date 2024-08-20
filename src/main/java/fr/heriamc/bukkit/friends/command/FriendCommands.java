package fr.heriamc.bukkit.friends.command;

import fr.heriamc.api.user.HeriaPlayer;
import fr.heriamc.api.user.rank.HeriaRank;
import fr.heriamc.api.user.resolver.HeriaPlayerResolver;
import fr.heriamc.bukkit.HeriaBukkit;
import fr.heriamc.bukkit.command.CommandArgs;
import fr.heriamc.bukkit.command.HeriaCommand;
import fr.heriamc.bukkit.friends.FriendRequest;
import fr.heriamc.bukkit.packet.BukkitPlayerMessagePacket;
import fr.heriamc.proxy.packet.ProxyPlayerMessagePacket;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class FriendCommands {

    private final HeriaBukkit bukkit;

    public FriendCommands(HeriaBukkit bukkit) {
        this.bukkit = bukkit;
    }

    @HeriaCommand(name = "friend", inGameOnly = true, power = HeriaRank.PLAYER, aliases = {"f", "friends"})
    public void onFriendCommand(CommandArgs args){
        Player player = args.getPlayer();

        player.sendMessage(" ");
        player.sendMessage("§fAide pour la commande §6/friend§f:");
        player.sendMessage(" §6» §e/friend add <joueur>§f: Ajouter/Accepter un ami");
        player.sendMessage(" §6» §e/friend remove <joueur>§f: Retirer un ami");
        player.sendMessage(" §6» §e/friend list <joueur>§f: Voir la liste de ses amis");
        player.sendMessage(" ");
    }

    @HeriaCommand(name = "friend.add", inGameOnly = true, power = HeriaRank.PLAYER, aliases = {"f.add", "friends.add"})
    public void onFriendAddCommand(CommandArgs args){
        Player player = args.getPlayer();
        HeriaPlayer heriaPlayer = this.bukkit.getApi().getPlayerManager().get(player.getUniqueId());

        if(args.length() != 1){
            player.sendMessage("§c/friend add <joueur>");
            return;
        }

        String targetName = args.getArgs(0);
        HeriaPlayerResolver targetResolver = bukkit.getApi().getResolverManager().get(targetName);

        if(targetResolver == null){
            player.sendMessage("§cCe joueur n'existe pas");
            return;
        }

        HeriaPlayer target = bukkit.getApi().getPlayerManager().get(targetResolver.getUuid());

        /*if(target.getId().equals(heriaPlayer.getId())){
            player.sendMessage("§cVous ne pouvez pas vous ajouter en ami");
            return;
        }*/

        if(target.getFriends().contains(player.getUniqueId())){
            player.sendMessage("§cVous êtes déjà ami avec ce joueur.");
            return;
        }

        for (UUID requestId : target.getPendingFriendsRequests()) {
            FriendRequest pendingRequest = this.bukkit.getFriendRequestManager().get(requestId);
            if(pendingRequest.getSender() == player.getUniqueId()){
                player.sendMessage("§cVous avez déjà une demande d'ami envoyée à ce joueur");
                return;
            }
        }

        for (UUID requestId : target.getSentFriendsRequests()) {
            FriendRequest sentRequest = this.bukkit.getFriendRequestManager().get(requestId);
            if(sentRequest.getReceiver() == player.getUniqueId()){

                this.bukkit.getFriendRequestManager().removeInPersistant(sentRequest);
                this.bukkit.getFriendRequestManager().remove(sentRequest);

                heriaPlayer.addFriend(target.getId());
                this.bukkit.getApi().getPlayerManager().save(heriaPlayer);

                target.addFriend(heriaPlayer.getId());
                this.bukkit.getApi().getPlayerManager().save(target);

                return;
            }
        }

        UUID uuid = UUID.randomUUID();
        FriendRequest loaded = this.bukkit.getFriendRequestManager().createOrLoad(uuid);
        loaded.setSender(player.getUniqueId());
        loaded.setReceiver(target.getId());
        this.bukkit.getFriendRequestManager().saveInPersistant(loaded);
        this.bukkit.getFriendRequestManager().save(loaded);

        target.addPendingFriendsRequest(uuid);
        this.bukkit.getApi().getPlayerManager().save(target);

        heriaPlayer.addSentFriendsRequest(uuid);
        this.bukkit.getApi().getPlayerManager().save(heriaPlayer);


        player.sendMessage("§aVotre demande d'ami pour " + target.getName() + " a bien été envoyée !");

        TextComponent mainText = new TextComponent("§fVous avez reçu une demande d'ami de §6" + player.getName() + "§f.\n");

        TextComponent accept = new TextComponent("§a[§a§l✓ §aAccepter] ");
        accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§aDevenir ami avec " + player.getName() + "?")));
        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend add " + player.getName()));

        TextComponent refuse = new TextComponent("§c[§c§l✗ §cRefuser]");
        refuse.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§cRefuser la demande de " + player.getName() + "?")));
        refuse.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend refuse " + player.getName()));

        /*Component component = Component.empty().append(Component.text("§fVous avez reçu une demande d'ami de §6" + player.getName() + "§f."))
                .appendNewline()
                .append(Component.text("§a[§a§l✓ §aAccepter]")
                        .hoverEvent(HoverEvent.showText(Component.text("§aDevenir ami avec " + player.getName() + "?")
                        .clickEvent(ClickEvent.runCommand("friend add " + player.getName())))))
                .appendSpace()
                .append(Component.text("§c[§c§l✗ §cRefuser]")
                        .hoverEvent(HoverEvent.showText(Component.text("§cRefuser la demande de " + player.getName() + "?")
                                .clickEvent(ClickEvent.runCommand("friend refuse " + player.getName())))));

        GsonComponentSerializer gsonSerializer = GsonComponentSerializer.builder().downsampleColors().build();*/

        this.bukkit.getApi().getMessaging().send(new BukkitPlayerMessagePacket(target.getId(), ComponentSerializer.toString(mainText,accept,refuse)));
    }

    @HeriaCommand(name = "friend.remove", inGameOnly = true, power = HeriaRank.PLAYER, aliases = {"f.remove", "friends.remove"})
    public void onFriendRemoveCommand(CommandArgs args){

    }

    @HeriaCommand(name = "friend.list", inGameOnly = true, power = HeriaRank.PLAYER, aliases = {"f.list", "friends.list"})
    public void onFriendListCommand(CommandArgs args){

    }
}
