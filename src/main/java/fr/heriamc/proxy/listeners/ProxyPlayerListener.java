package fr.heriamc.proxy.listeners;

import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerPing;
import fr.heriamc.api.server.HeriaServer;
import fr.heriamc.api.server.HeriaServerType;
import fr.heriamc.api.user.HeriaPlayer;
import fr.heriamc.api.user.resolver.HeriaPlayerResolver;
import fr.heriamc.proxy.HeriaProxy;
import net.kyori.adventure.text.Component;

import java.util.UUID;

public class ProxyPlayerListener {

    private final HeriaProxy proxy;

    public ProxyPlayerListener(HeriaProxy proxy) {
        this.proxy = proxy;
    }

    @Subscribe
    public void onPlayerPing(ProxyPingEvent event){
        ServerPing ping = event.getPing();

        ServerPing newPing = ping.asBuilder().maximumPlayers(500)
                .description(Component.text("heria developpement instance"))
                .build();

        event.setPing(newPing);
    }

    @Subscribe
    public void onLogin(LoginEvent event){
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        HeriaPlayer loaded = this.proxy.getApi().getPlayerManager().createOrLoad(uuid);
        loaded.setName(player.getUsername());
        this.proxy.getApi().getPlayerManager().save(loaded);

        HeriaPlayerResolver resolver = this.proxy.getApi().getResolverManager().createOrLoad(player.getUsername());

        if(resolver.getUuid() == null){
            resolver.setUuid(player.getUniqueId());
            this.proxy.getApi().getResolverManager().saveInPersistant(resolver);
            this.proxy.getApi().getResolverManager().save(resolver);
        }


        // maintenance
        if(loaded.getRank().getPower() < 10) {
            Component component = Component.text("Vous n'êtes pas dans la liste blanche du serveur");
            event.setResult(ResultedEvent.ComponentResult.denied(component));
            event.getPlayer().disconnect(component);
        }

    }

    @Subscribe
    public void onDisconnect(DisconnectEvent event){
        Player player = event.getPlayer();

        HeriaPlayer cached = this.proxy.getApi().getPlayerManager().getInCache(player.getUniqueId());
        if(cached == null){
            return;
        }

        this.proxy.getApi().getPlayerManager().saveInPersistant(cached);
        this.proxy.getApi().getPlayerManager().remove(cached.getIdentifier());
    }

    @Subscribe
    public void onServerChoose(PlayerChooseInitialServerEvent e){
        HeriaServer server = this.proxy.getApi().getServerManager().getWithLessPlayers(HeriaServerType.HUB);

        if(server == null){
            e.getPlayer().disconnect(Component.text("Aucun serveur hub n'a été trouvé"));
            e.setInitialServer(null);
            return;
        }

        RegisteredServer registeredServer = this.proxy.getServer().getServer(server.getName()).orElse(null);
        e.setInitialServer(registeredServer);
    }
}
