package fr.heriamc.bukkit.mod;

import fr.heriamc.bukkit.HeriaBukkit;
import fr.heriamc.bukkit.mod.history.HistoryCommand;
import fr.heriamc.bukkit.mod.rank.RankCommand;
import fr.heriamc.bukkit.mod.sanction.SanctionCommand;
import fr.heriamc.bukkit.mod.sanction.types.BanCommand;
import fr.heriamc.bukkit.mod.staff.ChatStaffCommand;
import fr.heriamc.bukkit.mod.staff.CheckCommand;
import fr.heriamc.bukkit.mod.staff.StaffCommand;
import fr.heriamc.bukkit.mod.staff.TeleportHereCommand;
import fr.heriamc.bukkit.report.command.chat.ReportChatListCommand;

public class ModManager {

    private final HeriaBukkit bukkit;

    public ModManager(HeriaBukkit bukkit) {
        this.bukkit = bukkit;

        bukkit.getCommandManager().registerCommand(new RankCommand(bukkit));
        bukkit.getCommandManager().registerCommand(new ReportChatListCommand(bukkit));
        bukkit.getCommandManager().registerCommand(new SanctionCommand(bukkit));
        bukkit.getCommandManager().registerCommand(new StaffCommand(bukkit));
        bukkit.getCommandManager().registerCommand(new TeleportHereCommand(bukkit));
        bukkit.getCommandManager().registerCommand(new HistoryCommand(bukkit));
        bukkit.getCommandManager().registerCommand(new CheckCommand(bukkit));
        bukkit.getCommandManager().registerCommand(new ChatStaffCommand(bukkit));

        bukkit.getCommandManager().registerCommand(new BanCommand(bukkit));
    }


}
