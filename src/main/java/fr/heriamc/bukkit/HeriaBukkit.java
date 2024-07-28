package fr.heriamc.bukkit;

import fr.heriamc.api.HeriaAPI;
import fr.heriamc.api.HeriaConfiguration;
import fr.heriamc.api.server.HeriaServer;
import fr.heriamc.api.server.HeriaServerStatus;
import fr.heriamc.api.utils.GsonUtils;
import fr.heriamc.api.utils.HeriaFileUtils;
import fr.heriamc.bukkit.command.HeriaCommandManager;
import fr.heriamc.bukkit.instance.InstanceCommand;
import fr.heriamc.bukkit.menu.HeriaMenuManager;
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
        server.setStatus(HeriaServerStatus.STARTED);
        this.api.getServerManager().save(server);

        this.api.getMessaging().send(new ServerRegisterPacket(server.getName(), server.getPort()));

        this.menuManager = new HeriaMenuManager(this);
        this.commandManager = new HeriaCommandManager(this);

        this.commandManager.registerCommand(new InstanceCommand(this));
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
}
