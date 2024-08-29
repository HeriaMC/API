package fr.heriamc.bukkit.mod.sanction;

import fr.heriamc.api.sanction.HeriaSanction;
import fr.heriamc.api.sanction.HeriaSanctionType;
import fr.heriamc.api.user.HeriaPlayer;
import fr.heriamc.api.user.rank.HeriaRank;
import fr.heriamc.bukkit.HeriaBukkit;
import fr.heriamc.bukkit.chat.HeriaChatMessage;
import fr.heriamc.bukkit.menu.HeriaMenu;
import fr.heriamc.bukkit.menu.confirm.ConfirmMenu;
import fr.heriamc.bukkit.packet.BukkitBroadcastMessagePacket;
import fr.heriamc.bukkit.utils.ItemBuilder;
import fr.heriamc.proxy.packet.ProxyPlayerKickPacket;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class SanctionConfirmMenu extends ConfirmMenu {

    private final HeriaPlayer target;
    private final UISanctionSubType sanctionType;
    private final HeriaChatMessage chatMessage;

    public SanctionConfirmMenu(Player player, HeriaBukkit bukkit, HeriaMenu before, HeriaPlayer target, UISanctionSubType sanctionType, HeriaChatMessage chatMessage) {
        super(player, "Confirmer la sanction de " + target.getName() + " ?", bukkit, before, actionPlayer -> {
            if(chatMessage != null){
                chatMessage.setReported(true);
                bukkit.getChatManager().save(chatMessage);
            }

            player.closeInventory();

            HeriaSanction sanction = bukkit.getApi().getSanctionManager().createOrLoad(UUID.randomUUID());
            sanction.setPlayer(target.getId());
            sanction.setType(sanctionType.getSanctionType());
            sanction.setBy(player.getUniqueId());
            sanction.setReason(sanctionType.getBanReason());
            sanction.setDuration(sanction.getDuration());

            bukkit.getApi().getSanctionManager().save(sanction);
            bukkit.getApi().getSanctionManager().saveInPersistant(sanction);

            if(sanction.getType() == HeriaSanctionType.BAN){
                String kickReason = bukkit.getApi().getSanctionManager().getKickMessage(sanction);
                bukkit.getApi().getMessaging().send(new ProxyPlayerKickPacket(target.getId(), kickReason));
            }

            if(sanction.getType() == HeriaSanctionType.MUTE){

            }

            String action = "banni";
            if (sanctionType.getSanctionType() == HeriaSanctionType.MUTE) action = "mute";

            String broadcast = "§a" + player.getName() + " §fa " + action + " §a" + target.getName() + " §fpour \"§c" + sanctionType.getBanReason() + "§f\"";
            bukkit.getApi().getMessaging().send(new BukkitBroadcastMessagePacket(broadcast, HeriaRank.HELPER.getPower()));
        });

        this.target = target;
        this.sanctionType = sanctionType;
        this.chatMessage = chatMessage;
    }

    @Override
    public void inventory(Inventory inventory) {
        setBorder(inventory, DyeColor.RED.getWoolData());
    }

    @Override
    public ItemBuilder getConfirmItem() {
        return super.getConfirmItem()
                .addLore(" ")
                .addLore("§8» §7Raison: " + sanctionType.getBanReason())
                .addLore(" ");
    }
}
