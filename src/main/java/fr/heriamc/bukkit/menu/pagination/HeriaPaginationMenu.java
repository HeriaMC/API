package fr.heriamc.bukkit.menu.pagination;

import fr.heriamc.api.utils.HeriaSkull;
import fr.heriamc.bukkit.menu.HeriaMenu;
import fr.heriamc.bukkit.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

public abstract class HeriaPaginationMenu<P> extends HeriaMenu {

    private final List<Integer> slots;
    private final Pagination<P> pagination;
    private int page = 0;

    private int leftSlot;
    private int rightSlot;

    public HeriaPaginationMenu(Player player, String name, int size, boolean update, List<Integer> slots, List<P> items) {
        super(player, name, size, update);
        this.slots = slots;
        this.pagination = new Pagination<>(slots.size(), items);

        this.leftSlot = 50;
        this.rightSlot = 51;
    }

    @Override
    public void contents(Inventory inv) {
        inventory(inv);
        int iteration = 0;
        for (P p : pagination.getPageContent(page)) {
            int slot = this.slots.get(iteration);
            ItemBuilder itemBuilder = item(p, slot, this.page);

            if(itemBuilder.getClickEvent() != null){
                this.insertInteractItem(inv, slot, itemBuilder);
            } else {
                inv.setItem(slot, itemBuilder.build());
            }

            iteration++;
        }

        this.insertInteractItem(inv, leftSlot, getLeftArrow().onClick(event -> {
            if(pagination.existsPage(this.page - 1)){
                this.page--;
                updateMenu();
            }
        }));

        this.insertInteractItem(inv, rightSlot, getRightArrow().onClick(event -> {
            if(pagination.existsPage(this.page + 1)){
                this.page++;
                updateMenu();
            }
        }));


    }

    public abstract void inventory(Inventory inventory);

    protected abstract ItemBuilder item(P data, int slot, int page);

    public ItemBuilder getLeftArrow(){
        return new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3)
                .setSkullURL(HeriaSkull.GRAY_BACKWARDS.getURL())
                .setName("§8« §7Page précédente");
    }

    public ItemBuilder getRightArrow(){
        return new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3)
                .setSkullURL(HeriaSkull.GRAY_FORWARD.getURL())
                .setName("§8» §7Page suivante");
    }

    public HeriaPaginationMenu<P> setLeftSlot(int leftSlot) {
        this.leftSlot = leftSlot;
        return this;
    }

    public HeriaPaginationMenu<P> setRightSlot(int rightSlot) {
        this.rightSlot = rightSlot;
        return this;
    }
}
