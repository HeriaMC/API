package fr.heriamc.proxy.queue;

import com.velocitypowered.api.proxy.Player;
import fr.heriamc.api.game.GameState;
import fr.heriamc.api.game.HeriaGameInfo;
import fr.heriamc.api.game.HeriaGamesList;
import fr.heriamc.api.messaging.packet.HeriaPacket;
import fr.heriamc.api.queue.HeriaQueue;
import fr.heriamc.api.queue.HeriaQueue.QueueServerType;
import fr.heriamc.api.queue.HeriaQueue.QueueType;
import fr.heriamc.api.server.HeriaServer;
import fr.heriamc.api.server.HeriaServerType;
import fr.heriamc.api.user.HeriaPlayer;
import fr.heriamc.api.game.size.GameSize;
import fr.heriamc.proxy.HeriaProxy;
import fr.heriamc.proxy.pool.HeriaPool;
import fr.heriamc.proxy.utils.AutoUpdatingList;
import fr.heriamc.proxy.utils.ProxyPacketUtil;
import net.kyori.adventure.text.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class HeriaQueueHandler {

    private final HeriaProxy proxy = HeriaProxy.get();
    private final AutoUpdatingList<UUID> playerQueue = new AutoUpdatingList<>(1000, Comparator.comparing(this::calculatePriority));
    private long lastProcess;

    private final HeriaQueue queue;
    private HeriaPool serversPool;

    public static final int MAX_SERVER_SIZE = 50;

    public HeriaQueueHandler(String server){
        this(new HeriaQueue(UUID.randomUUID(), QueueType.KNOWN, QueueServerType.SERVER, null, null, server, null, new HashSet<>()));
    }

    public HeriaQueueHandler(String server, String gameName){
        this(new HeriaQueue(UUID.randomUUID(), QueueType.KNOWN, QueueServerType.GAME, null, null, server, gameName, new HashSet<>()));
    }

    public HeriaQueueHandler(HeriaServerType serverType){
        this(new HeriaQueue(UUID.randomUUID(), QueueType.UNKNOWN, QueueServerType.SERVER, serverType, null, null, null, new HashSet<>()));
        this.serversPool = proxy.getPoolManager().getServerPool(serverType);
    }

    public HeriaQueueHandler(HeriaServerType serverType, GameSize gameSize){
        this(new HeriaQueue(UUID.randomUUID(), QueueType.UNKNOWN, QueueServerType.GAME, serverType, gameSize, null, null, new HashSet<>()));
        this.serversPool = proxy.getPoolManager().getGamePool(serverType, gameSize);
    }

    private HeriaQueueHandler(HeriaQueue queue) {
        this.queue = queue;

        proxy.getServer().getScheduler().buildTask(proxy, (scheduledTask -> {
            updateQueue();
            processPlayers();
        })).repeat(250, TimeUnit.MILLISECONDS).schedule();
    }

    public void disable(){

    }

    public void processPlayers(){
        //kill queue if empty more than a minute
        if(this.playerQueue.size() == 0 & System.currentTimeMillis() - this.lastProcess >= TimeUnit.MINUTES.toMillis(1)){
            this.disable();
        }

        for (UUID player : this.playerQueue.getList()) {
            processPlayer(player);
        }

        if(this.playerQueue.size() > 0){
            this.lastProcess = System.currentTimeMillis();
        }

    }

    public void processPlayer(UUID player){
        if(this.serversPool == null){

            if(queue.getQueueServerType() == QueueServerType.GAME){
                HeriaGamesList heriaGamesList = proxy.getApi().getHeriaGameManager().get(queue.getServer());

                HeriaGameInfo gameInfo = null;
                for (HeriaGameInfo game : heriaGamesList.getGames()) {
                    if(game.getGameName().equals(queue.getGame())){
                        gameInfo = game;
                    }
                }

                if(gameInfo == null){
                    return;
                }

                boolean spectator = !gameInfo.getState().is(GameState.WAIT, GameState.ALWAYS_PLAYING, GameState.STARTING)
                        || gameInfo.getPlayers().size() >= gameInfo.getGameSize().getMaxPlayer();

                List<HeriaPacket> packets = ProxyPacketUtil.buildJoinGame(player, queue.getServer(), queue.getGame(), spectator);
                for (HeriaPacket packet : packets) {
                    proxy.getApi().getMessaging().send(packet);
                }

                this.removePlayer(player);
            } else if(queue.getQueueServerType() == QueueServerType.SERVER){
                HeriaServer server = proxy.getApi().getServerManager().get(queue.getServer());

                if(server.getConnected().size() >= MAX_SERVER_SIZE){
                    Player connected = proxy.getServer().getPlayer(player).orElse(null);

                    if(connected != null){
                        connected.sendMessage(Component.text("Vous avez été exclu de la file d'attente vers " + queue.getServer() + ": Serveur complet."));
                    }

                    this.removePlayer(player);
                    return;
                }

                List<HeriaPacket> packets = ProxyPacketUtil.buildJoinServer(player, queue.getServer());
                for (HeriaPacket packet : packets) {
                    proxy.getApi().getMessaging().send(packet);
                }

                this.removePlayer(player);
            }

            return;
        }

        if(this.serversPool.isAvailable()){
            List<HeriaPacket> packets = this.serversPool.createPackets(player);
            for (HeriaPacket packet : packets) {
                proxy.getApi().getMessaging().send(packet);
            }
            this.removePlayer(player);
        }
    }

    private int calculatePriority(UUID player) {
        HeriaPlayer account = proxy.getApi().getPlayerManager().get(player);
        return account.getRank().getTabPriority();
    }

    public void addPlayer(UUID player) {
        HeriaPlayer heriaPlayer = proxy.getApi().getPlayerManager().get(player);
        heriaPlayer.setQueue(this.queue.getId());
        heriaPlayer.setJoinQueueTime(System.currentTimeMillis());
        proxy.getApi().getPlayerManager().save(heriaPlayer);

        this.playerQueue.add(player);
        updateQueue();
    }

    public void removePlayer(UUID player) {
        HeriaPlayer heriaPlayer = proxy.getApi().getPlayerManager().get(player);
        heriaPlayer.setQueue(null);
        proxy.getApi().getPlayerManager().save(heriaPlayer);

        this.playerQueue.remove(player);
        updateQueue();
    }

    public void updateQueue(){
        this.queue.getPlayers().clear();
        this.queue.getPlayers().addAll(this.playerQueue.getList());

        this.proxy.getApi().getQueueManager().save(this.queue);
    }

    public HeriaQueue getQueue() {
        return queue;
    }
}
