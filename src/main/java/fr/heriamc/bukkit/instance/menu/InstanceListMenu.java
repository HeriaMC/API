package fr.heriamc.bukkit.instance.menu;

import fr.heriamc.api.server.HeriaServer;
import fr.heriamc.bukkit.HeriaBukkit;
import fr.heriamc.bukkit.menu.HeriaMenu;
import fr.heriamc.bukkit.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InstanceListMenu extends HeriaMenu {

    private final HeriaBukkit bukkit;

    private final int[] instancesSlots = new int[]{
            10,11,12,13,14,15,16,
            19,20,21,22,23,24,25,
            28,29,30,31,32,33,34
    };

    private final Map<Integer, HeriaServer> instances = new HashMap<>();

    public InstanceListMenu(Player player, HeriaBukkit bukkit) {
        super(player, "Instances", 54, true);
        this.bukkit = bukkit;
    }

    @Override
    public void contents(Inventory inv) {
        List<HeriaServer> cacheInstances = bukkit.getApi().getServerManager().getAllInCache();

        int iteration = 0;
        for (HeriaServer instance : cacheInstances) {
            inv.setItem(instancesSlots[iteration], new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3)
                    .setSkullURL(instance.getStatus().getSkull().getURL())
                    .setName(instance.getStatus().getColor() + instance.getName())
                    .addLore(" ")
                    .addLore(" §8» §7")
                    .build());
            iteration++;
        }
    }
}
