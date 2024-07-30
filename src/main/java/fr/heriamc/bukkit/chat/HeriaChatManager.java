package fr.heriamc.bukkit.chat;

import fr.heriamc.api.data.CacheDataManager;
import fr.heriamc.api.data.redis.RedisConnection;
import fr.heriamc.bukkit.HeriaBukkit;
import fr.heriamc.bukkit.chat.command.ReportChatCommand;

import java.util.UUID;

public class HeriaChatManager extends CacheDataManager<UUID, HeriaChatMessage> {

    private final HeriaBukkit bukkit;

    public HeriaChatManager(RedisConnection redisConnection, HeriaBukkit bukkit) {
        super(redisConnection, "chat");
        this.bukkit = bukkit;

        bukkit.getServer().getPluginManager().registerEvents(new HeriaChatListener(bukkit), bukkit);
        bukkit.getCommandManager().registerCommand(new ReportChatCommand(bukkit));
    }


}
