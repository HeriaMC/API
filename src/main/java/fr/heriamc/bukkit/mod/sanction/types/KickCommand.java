package fr.heriamc.bukkit.mod.sanction.types;

import fr.heriamc.api.sanction.HeriaSanction;
import fr.heriamc.api.sanction.HeriaSanctionType;
import fr.heriamc.api.user.HeriaPlayer;
import fr.heriamc.api.user.rank.HeriaRank;
import fr.heriamc.api.user.resolver.HeriaPlayerResolver;
import fr.heriamc.bukkit.HeriaBukkit;
import fr.heriamc.bukkit.command.CommandArgs;
import fr.heriamc.bukkit.command.HeriaCommand;
import fr.heriamc.bukkit.packet.BukkitBroadcastMessagePacket;
import fr.heriamc.proxy.packet.ProxyPlayerKickPacket;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.UUID;

public class KickCommand {

    private final HeriaBukkit bukkit;

    public KickCommand(HeriaBukkit bukkit) {
        this.bukkit = bukkit;
    }

    @HeriaCommand(name = "kick", power = HeriaRank.HELPER)
    public void onKickCommand(CommandArgs args){
        Player player = args.getPlayer();
        HeriaPlayer heriaPlayer = bukkit.getApi().getPlayerManager().get(player.getUniqueId());

        if(args.getArgs().length < 2){
            player.sendMessage("§c/kick <joueur> <raison>");
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

        if(target.getRank().getPower() > heriaPlayer.getRank().getPower()){
            player.sendMessage("§cVous ne pouvez pas warn quelqu'un en dessus de vous");
            return;
        }

        String reason = String.join(" ", Arrays.copyOfRange(args.getArgs(), 1, args.getArgs().length));

        HeriaSanction loaded = bukkit.getApi().getSanctionManager().createOrLoad(UUID.randomUUID());
        loaded.setPlayer(target.getId());
        loaded.setBy(player.getUniqueId());
        loaded.setReason(reason);
        loaded.setType(HeriaSanctionType.KICK);

        bukkit.getApi().getSanctionManager().save(loaded);
        bukkit.getApi().getSanctionManager().saveInPersistant(loaded);

        String kickMessage = bukkit.getApi().getSanctionManager().getKickMessage(loaded);
        bukkit.getApi().getMessaging().send(new ProxyPlayerKickPacket(target.getId(), kickMessage));

        String broadcast = "§a" + player.getName() + " §fa kick §a" + target.getName() + " §fpour \"§c" + reason + "§f\"";
        bukkit.getApi().getMessaging().send(new BukkitBroadcastMessagePacket(broadcast, HeriaRank.HELPER.getPower()));
    }


}