package fr.heriamc.bukkit.tab;

import fr.heriamc.bukkit.HeriaBukkit;
import fr.heriamc.bukkit.utils.Title;
import org.bukkit.entity.Player;

public class TabUpdater implements Runnable {

    private final HeriaBukkit heriaBukkit;

    private final String serverName;

    public TabUpdater(HeriaBukkit heriaBukkit) {
        this.heriaBukkit = heriaBukkit;

        this.serverName = heriaBukkit.getInstanceName();
    }

    @Override
    public void run() {
        for (Player player : heriaBukkit.getServer().getOnlinePlayers()) {
            Title.sendTabTitle(player, "\n§r§e§l» §6§lHERIAMC §e§l«\n§r §7Vous êtes connecté sur §e" + serverName + " \n§r",
                    "\n§r §7Un §csouci §7? Utilise §c/report \n§r§6play.heria-mc.fr\n§r");
        }
    }
}
