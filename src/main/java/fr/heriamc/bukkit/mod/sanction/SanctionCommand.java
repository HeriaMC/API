package fr.heriamc.bukkit.mod.sanction;

import fr.heriamc.api.user.HeriaPlayer;
import fr.heriamc.api.user.rank.HeriaRank;
import fr.heriamc.api.user.resolver.HeriaPlayerResolver;
import fr.heriamc.bukkit.HeriaBukkit;
import fr.heriamc.bukkit.command.CommandArgs;
import fr.heriamc.bukkit.command.HeriaCommand;
import org.bukkit.entity.Player;

public class SanctionCommand {

    private final HeriaBukkit heriaBukkit;

    public SanctionCommand(HeriaBukkit heriaBukkit) {
        this.heriaBukkit = heriaBukkit;
    }

    @HeriaCommand(name = "sanction", inGameOnly = true, power = HeriaRank.HELPER, aliases = {"ss"})
    public void onSanctionCommand(CommandArgs args){
        Player player = args.getPlayer();
        if(args.getArgs().length != 1){
            player.sendMessage("§c/sanction <joueur>");
            return;
        }

        String targetName = args.getArgs(0);
        HeriaPlayerResolver resolver = heriaBukkit.getApi().getResolverManager().get(targetName);

        if(resolver == null){
            player.sendMessage("§cCe joueur n'existe pas");
            return;
        }

        HeriaPlayer heriaPlayer = heriaBukkit.getApi().getPlayerManager().get(resolver.getUuid());

        if(heriaPlayer == null){
            player.sendMessage("§cCe joueur n'existe plus, il a surement changé de pseudonyme");
            return;
        }

        heriaBukkit.getMenuManager().open(new SanctionMenu(player, heriaBukkit, heriaPlayer));
    }
}
