package fr.heriamc.bukkit.instance;

import fr.heriamc.api.user.rank.HeriaRank;
import fr.heriamc.bukkit.HeriaBukkit;
import fr.heriamc.bukkit.command.CommandArgs;
import fr.heriamc.bukkit.command.HeriaCommand;
import fr.heriamc.bukkit.instance.menu.InstanceListMenu;
import org.bukkit.entity.Player;

public class InstanceCommand {

    private final HeriaBukkit bukkit;

    public InstanceCommand(HeriaBukkit heriaBukkit) {
        this.bukkit = heriaBukkit;
    }

    @HeriaCommand(name = "instances", power = HeriaRank.ADMIN, inGameOnly = true)
    public void onCommand(CommandArgs args){
        Player player = args.getPlayer();
        this.bukkit.getMenuManager().open(new InstanceListMenu(player, bukkit));
    }

}
