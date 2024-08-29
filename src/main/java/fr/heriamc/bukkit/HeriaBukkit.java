package fr.heriamc.bukkit;

import fr.heriamc.api.HeriaAPI;
import fr.heriamc.api.HeriaConfiguration;
import fr.heriamc.api.messaging.packet.HeriaPacketChannel;
import fr.heriamc.api.server.HeriaServer;
import fr.heriamc.api.server.HeriaServerStatus;
import fr.heriamc.api.utils.GsonUtils;
import fr.heriamc.api.utils.HeriaFileUtils;
import fr.heriamc.bukkit.chat.HeriaChatManager;
import fr.heriamc.bukkit.chat.command.PrivateMessageCommand;
import fr.heriamc.bukkit.command.HeriaCommandManager;
import fr.heriamc.bukkit.friends.FriendRequestManager;
import fr.heriamc.bukkit.friends.command.FriendCommands;
import fr.heriamc.bukkit.game.HeriaGameManager;
import fr.heriamc.bukkit.instance.InstanceCommand;
import fr.heriamc.bukkit.menu.HeriaMenuManager;
import fr.heriamc.bukkit.mod.ModManager;
import fr.heriamc.bukkit.packet.BukkitPacketReceiver;
import fr.heriamc.bukkit.report.HeriaReportManager;
import fr.heriamc.bukkit.tab.TabUpdater;
import fr.heriamc.proxy.packet.ServerRegisterPacket;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.author.Author;
import org.bukkit.plugin.java.annotation.plugin.author.Authors;

import java.io.File;

@Plugin(name = "HeriaAPI", version = "1.0.0")
@Authors(@Author("Karaam_"))
@ApiVersion(ApiVersion.Target.v1_13)
public class HeriaBukkit extends JavaPlugin {

    private static HeriaBukkit instance;
    private HeriaAPI api;

    private HeriaMenuManager menuManager;
    private HeriaCommandManager commandManager;
    private HeriaChatManager chatManager;
    private HeriaReportManager reportManager;
    private HeriaGameManager heriaGameManager;
    private FriendRequestManager friendRequestManager;

    @Override
    public void onEnable() {
        instance = this;

        this.getDataFolder().mkdir();

        File configFile = new File(this.getDataFolder(), "config.json");
        HeriaConfiguration config = HeriaFileUtils.fromJsonFile(configFile, GsonUtils.get(), HeriaConfiguration.class);

        if(config == null){
            this.getServer().getLogger().severe("Error while loading the configuration file. is it present ?");
            this.getServer().shutdown();
            return;
        }

        this.api = HeriaAPI.createHeriaAPI(config);


        HeriaServer server = this.api.getServerManager().get(this.getInstanceName());
        this.api.getMessaging().registerReceiver(HeriaPacketChannel.API, new BukkitPacketReceiver(this));
        this.api.getMessaging().send(new ServerRegisterPacket(server.getName(), server.getPort()));

        server.setStatus(HeriaServerStatus.STARTED);
        this.api.getServerManager().save(server);

        this.menuManager = new HeriaMenuManager(this);
        this.commandManager = new HeriaCommandManager(this);
        this.chatManager = new HeriaChatManager(this.getApi().getRedisConnection(), this);
        this.friendRequestManager = new FriendRequestManager(this.getApi().getRedisConnection(), this.getApi().getMongoConnection());
        this.heriaGameManager = new HeriaGameManager(this.getApi().getRedisConnection());
        this.reportManager = new HeriaReportManager(this.getApi().getRedisConnection(), this.getApi().getMongoConnection());

        this.commandManager.registerCommand(new InstanceCommand(this));
        this.commandManager.registerCommand(new PrivateMessageCommand(this));
        this.commandManager.registerCommand(new FriendCommands(this));
        this.getServer().getScheduler().runTaskTimer(this, new TabUpdater(this), 0L, 20L);

        new ModManager(this);
    }

    @Override
    public void onDisable() {
        if(this.api != null) this.api.onDisable();
    }

    public static HeriaBukkit get(){
        return instance;
    }

    public String getInstanceName(){
        return this.getServer().getMotd();
    }

    public HeriaAPI getApi() {
        return api;
    }

    public HeriaMenuManager getMenuManager() {
        return menuManager;
    }

    public HeriaCommandManager getCommandManager() {
        return commandManager;
    }

    public HeriaChatManager getChatManager() {
        return chatManager;
    }

    public FriendRequestManager getFriendRequestManager() {
        return friendRequestManager;
    }

    public HeriaReportManager getReportManager() {
        return reportManager;
    }
}
