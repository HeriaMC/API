package fr.heriamc.bukkit.vip.tag;

import fr.heriamc.api.user.HeriaPlayer;
import fr.heriamc.api.user.rank.HeriaRank;
import fr.heriamc.bukkit.HeriaBukkit;
import fr.heriamc.bukkit.command.CommandArgs;
import fr.heriamc.bukkit.command.HeriaCommand;
import org.bukkit.entity.Player;

public class TagCommand {

    private final HeriaBukkit bukkit;

    public TagCommand(HeriaBukkit heriaBukkit) {
        this.bukkit = heriaBukkit;
    }

    @HeriaCommand(name = "tag", power = HeriaRank.VIP)
    public void onTagCommand(CommandArgs args){
        Player player = args.getPlayer();

        HeriaPlayer heriaPlayer = bukkit.getApi().getPlayerManager().get(player.getUniqueId());

        if(heriaPlayer.isRemovedTag()){
            heriaPlayer.setRemovedTag(false);
            player.sendMessage("§aVous avez réactivé votre grade");
            bukkit.getApi().getPlayerManager().save(heriaPlayer);
            bukkit.getServer().getPluginManager().callEvent(new TagEvent(player, false));
            return;
        }

        heriaPlayer.setRemovedTag(true);
        player.sendMessage("§cVous avez désactivé votre grade");
        bukkit.getApi().getPlayerManager().save(heriaPlayer);
        bukkit.getServer().getPluginManager().callEvent(new TagEvent(player, true));

    }
}