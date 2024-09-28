package fr.heriamc.bukkit.mod.sanction;

import fr.heriamc.api.user.HeriaPlayer;
import fr.heriamc.bukkit.HeriaBukkit;
import fr.heriamc.bukkit.menu.HeriaMenu;
import fr.heriamc.bukkit.utils.ItemBuilder;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class SanctionMenu extends HeriaMenu {

    private final HeriaBukkit heriaBukkit;
    private final HeriaPlayer target;

    public SanctionMenu(Player player, HeriaBukkit heriaBukkit, HeriaPlayer target) {
        super(player, "Sanction pour " + target.getName(), 54, false);
        this.heriaBukkit = heriaBukkit;
        this.target = target;
    }

    @Override
    public void contents(Inventory inv) {
        setBorder(inv, DyeColor.RED.getWoolData());
        for (UISanctionType value : UISanctionType.values()) {
            this.insertInteractItem(inv, value.getSlot(), value.getItem()
                    .onClick(event -> {
                        if(value != UISanctionType.CHAT){
                            //TODO: check player permissions (above helper ?)
                        }

                        heriaBukkit.getMenuManager().open(new SubSanctionMenu(getPlayer(), heriaBukkit, target, value, this));
                    }));
        }

        this.insertInteractItem(inv, 49, new ItemBuilder(Material.DARK_OAK_DOOR_ITEM)
                .setName("§cRetour en arrière")
                .onClick(event -> {
                    getPlayer().closeInventory();
                }));
    }
}
