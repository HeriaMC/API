package fr.heriamc.proxy.listeners;

import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.player.KickedFromServerEvent;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.event.player.PlayerClientBrandEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerPing;
import fr.heriamc.api.sanction.HeriaSanction;
import fr.heriamc.api.sanction.HeriaSanctionType;
import fr.heriamc.api.server.HeriaServer;
import fr.heriamc.api.server.HeriaServerType;
import fr.heriamc.api.user.HeriaPlayer;
import fr.heriamc.api.user.resolver.HeriaPlayerResolver;
import fr.heriamc.proxy.HeriaProxy;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import java.util.List;
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
                .description(Component.text("  §6§l«§b-§6§l» HeriaMC §8▪ §eServeur Mini-Jeux §8(§b1.8+§8) §6§l«§b-§6§l»\n" +
                                                   "     §8- §cServeur en maintenance temporaire §8-"))
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

        HeriaPlayerResolver resolver = this.proxy.getApi().getResolverManager().createOrLoad(player.getUsername().toLowerCase());

        if(resolver.getUuid() == null){
            resolver.setUuid(player.getUniqueId());
            this.proxy.getApi().getResolverManager().saveInPersistant(resolver);
            this.proxy.getApi().getResolverManager().save(resolver);
        }


        this.proxy.getApi().getUnlockableManager().createOrLoad(player.getUniqueId());

        List<HeriaSanction> bans = this.proxy.getApi().getSanctionManager().getActiveSanctions(uuid, HeriaSanctionType.BAN);
        if(!bans.isEmpty()){
            HeriaSanction active = bans.get(0);

            Component banComponent = PlainTextComponentSerializer.plainText().deserialize(
                    this.proxy.getApi().getSanctionManager().getKickMessage(active));

            event.setResult(ResultedEvent.ComponentResult.denied(banComponent));
            event.getPlayer().disconnect(banComponent);
            return;
        }

        // maintenance
        if(loaded.getRank().getPower() < 10) {
            Component component = Component.text("Vous n'êtes pas dans la liste blanche du serveur");
            event.setResult(ResultedEvent.ComponentResult.denied(component));
            event.getPlayer().disconnect(component);
            return;
        }

    }

    @Subscribe
    public void onClientBrand(PlayerClientBrandEvent event){
        Player player = event.getPlayer();
        HeriaPlayer cached = this.proxy.getApi().getPlayerManager().getInCache(player.getUniqueId());

        if(cached != null){
            cached.setClientBrand(event.getBrand());
            if(event.getBrand() == null) cached.setClientBrand("Inconnu (cheater probablement?)");

            this.proxy.getApi().getPlayerManager().save(cached);
        }
    }

    @Subscribe
    public void onDisconnect(DisconnectEvent event){
        Player player = event.getPlayer();

        HeriaPlayer cached = this.proxy.getApi().getPlayerManager().getInCache(player.getUniqueId());

        if(cached == null){
            return;
        }

        System.out.println("Le joueur " + player.getUsername() + " s'est déconnecté et a été trouvé dans le cache. sauvegarde de son profil...");

        cached.setConnectedTo(null);

        this.proxy.getApi().getPlayerManager().saveInPersistant(cached);
        this.proxy.getApi().getPlayerManager().remove(cached.getIdentifier());
        System.out.println("Sauvegarde réussie.");
    }

    @Subscribe
    public void onServerChoose(PlayerChooseInitialServerEvent e){
        HeriaServer server = this.proxy.getApi().getServerManager().getReadyWithLessPlayers(HeriaServerType.HUB);

        if(server == null){
            e.getPlayer().disconnect(Component.text("§cAucun serveur hub n'a été trouvé"));
            e.setInitialServer(null);
            return;
        }

        RegisteredServer registeredServer = this.proxy.getServer().getServer(server.getName()).orElse(null);
        e.setInitialServer(registeredServer);
    }

    @Subscribe
    public void onServerJoin(ServerConnectedEvent e){
        Player player = e.getPlayer();
        String name = e.getServer().getServerInfo().getName();

        HeriaPlayer heriaPlayer = proxy.getApi().getPlayerManager().get(player.getUniqueId());

        if(heriaPlayer == null){
            return;
        }

        heriaPlayer.setConnectedTo(name);
        proxy.getApi().getPlayerManager().save(heriaPlayer);

        HeriaServer heriaServer = proxy.getApi().getServerManager().get(name);

        if(heriaServer == null) {
            return;
        }

        heriaServer.getConnected().add(player.getUniqueId());
        proxy.getApi().getServerManager().save(heriaServer);
    }

    @Subscribe
    public void onKick(KickedFromServerEvent event){
        if(!(event.getResult() instanceof KickedFromServerEvent.RedirectPlayer playerRedirection)){
            return;
        }

        HeriaServer server = proxy.getApi().getServerManager().getWithLessPlayers(HeriaServerType.HUB);
        RegisteredServer registeredServer = proxy.getServer().getServer(server.getName()).orElse(null);

        if(registeredServer == null){
            return;
        }

        event.setResult(KickedFromServerEvent.RedirectPlayer.create(registeredServer, Component.text("§cVotre serveur précédent a rencontré un problème, vous avez été redirigé vers " + server.getName())));
    }
}
