package fr.heriamc.bukkit.prefix.commands;

import fr.heriamc.api.user.rank.HeriaRank;
import fr.heriamc.bukkit.HeriaBukkit;
import fr.heriamc.bukkit.command.CommandArgs;
import fr.heriamc.bukkit.command.HeriaCommand;
import fr.heriamc.bukkit.prefix.menu.PrefixRequestsMenu;
import org.bukkit.entity.Player;

public class BrowsePrefixesCommand {

    private final HeriaBukkit bukkit;

    public BrowsePrefixesCommand(HeriaBukkit bukkit) {
        this.bukkit = bukkit;
    }

    @HeriaCommand(name = "browseprefixes", power = HeriaRank.SUPER_MOD)
    public void onBrowsePrefixesCommand(CommandArgs args){
        Player player = args.getPlayer();

        bukkit.getMenuManager().open(new PrefixRequestsMenu(player, bukkit));
    }
}
