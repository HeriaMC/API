package fr.heriamc.bukkit.report.command.action;

import fr.heriamc.api.user.rank.HeriaRank;
import fr.heriamc.bukkit.HeriaBukkit;
import fr.heriamc.bukkit.command.CommandArgs;
import fr.heriamc.bukkit.command.HeriaCommand;

public class ReportBugCommand {

    private final HeriaBukkit bukkit;

    public ReportBugCommand(HeriaBukkit bukkit) {
        this.bukkit = bukkit;
    }

    @HeriaCommand(name = "bug", power = HeriaRank.PLAYER)
    public void onBugCommand(CommandArgs args){
        

    }
}
