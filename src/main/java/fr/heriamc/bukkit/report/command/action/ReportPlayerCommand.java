package fr.heriamc.bukkit.report.command.action;

import fr.heriamc.api.user.rank.HeriaRank;
import fr.heriamc.bukkit.HeriaBukkit;
import fr.heriamc.bukkit.command.CommandArgs;
import fr.heriamc.bukkit.command.HeriaCommand;

public class ReportPlayerCommand {

    private final HeriaBukkit bukkit;

    public ReportPlayerCommand(HeriaBukkit bukkit) {
        this.bukkit = bukkit;
    }

    @HeriaCommand(name = "report", power = HeriaRank.PLAYER, description = "Vous permet de report un joueur")
    public void onReportCommand(CommandArgs args){

    }
}
