package fr.heriamc.bukkit.mod.staff;

import fr.heriamc.api.user.HeriaPlayer;
import fr.heriamc.api.user.rank.HeriaRank;
import fr.heriamc.api.user.resolver.HeriaPlayerResolver;
import fr.heriamc.bukkit.HeriaBukkit;
import fr.heriamc.bukkit.command.CommandArgs;
import fr.heriamc.bukkit.command.HeriaCommand;
import fr.heriamc.bukkit.utils.TimeUtils;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

public class CheckCommand {

    private final HeriaBukkit bukkit;

    public CheckCommand(HeriaBukkit bukkit) {
        this.bukkit = bukkit;
    }

    @HeriaCommand(name = "check", power = HeriaRank.MOD)
    public void onCheckCommand(CommandArgs args){
        Player player = args.getPlayer();

        if(args.getArgs().length != 1){
            player.sendMessage("§c/check <joueur>");
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

        player.sendMessage("§7Check de §6" + target.getName());
        player.sendMessage(" §8» §7Grade: " + target.getRank().getPrefix());
        player.sendMessage(" §8» §7Première conn.: §3" + TimeUtils.convertMilliSecondsToFormattedDate(target.getFirstConnection()));
        player.sendMessage(" §8» §7Client: §6" + target.getClientBrand());
        player.sendMessage(" §8» §7Nick?: pas encore dev");
    }
}
