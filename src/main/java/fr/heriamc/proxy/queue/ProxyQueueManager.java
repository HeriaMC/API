package fr.heriamc.proxy.queue;

import fr.heriamc.api.game.size.GameSize;
import fr.heriamc.api.messaging.packet.HeriaPacketChannel;
import fr.heriamc.api.queue.HeriaQueue;
import fr.heriamc.api.server.HeriaServerType;
import fr.heriamc.proxy.HeriaProxy;
import fr.heriamc.proxy.queue.packet.QueueJoinPacket;
import fr.heriamc.proxy.queue.packet.QueueLeavePacket;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ProxyQueueManager {

    private final HeriaProxy proxy;
    private final Set<HeriaQueueHandler> queues = ConcurrentHashMap.newKeySet();

    public ProxyQueueManager(HeriaProxy proxy) {
        this.proxy = proxy;
        System.out.println("ProxyQueueManager initialized.");
        this.proxy.getApi().getMessaging().registerReceiver(HeriaPacketChannel.QUEUE, new HeriaQueueReceiver(this));
    }

    public void onJoin(QueueJoinPacket joinPacket) {
        System.out.println("Player joining queue with packet: " + joinPacket);

        HeriaQueueHandler finalHandler = null;

        HeriaQueueHandler handler = get(joinPacket);
        if (handler != null) {
            System.out.println("Existing handler found for join packet.");
            finalHandler = handler;
        }

        if (finalHandler == null) {
            System.out.println("No existing handler found, creating new handler...");
            if (joinPacket.getServerName() != null) {
                if (joinPacket.getGameName() != null) {
                    finalHandler = new HeriaQueueHandler(joinPacket.getServerName(), joinPacket.getGameName());
                    System.out.println("Handler created with server name and game name.");
                } else {
                    finalHandler = new HeriaQueueHandler(joinPacket.getServerName());
                    System.out.println("Handler created with server name only.");
                }
            } else {
                if (joinPacket.getGameSize() != null) {
                    finalHandler = new HeriaQueueHandler(joinPacket.getServerType(), joinPacket.getGameSize());
                    System.out.println("Handler created with server type and game size.");

                } else {
                    finalHandler = new HeriaQueueHandler(joinPacket.getServerType());
                    System.out.println("Handler created with server type only.");
                }
            }

            this.queues.add(finalHandler);
            System.out.println("Handler added to set");
        }

        finalHandler.addPlayer(joinPacket.getPlayer());
        System.out.println("Player added to handler: " + finalHandler);
    }

    public void onLeave(QueueLeavePacket leavePacket) {
        System.out.println("Player leaving queue with packet: " + leavePacket);
        // Implémentation potentielle pour gérer le départ d'un joueur
    }

    public HeriaQueueHandler get(QueueJoinPacket joinPacket) {
        System.out.println("Retrieving handler for join packet...");
        return get(joinPacket.getServerName(), joinPacket.getGameName(), joinPacket.getServerType(), joinPacket.getGameSize());
    }

    public HeriaQueueHandler get(String server, String game, HeriaServerType serverType, GameSize gameSize) {
        System.out.println("Searching for handler with server: " + server + ", game: " + game + ", serverType: " + serverType + ", gameSize: " + gameSize);

        for (HeriaQueueHandler handler : this.queues) {
            HeriaQueue queue = handler.getQueue();

            boolean serverMatch = (queue.getServer() == null && server == null) || (queue.getServer() != null && queue.getServer().equals(server));
            boolean gameMatch = (queue.getGame() == null && game == null) || (queue.getGame() != null && queue.getGame().equals(game));
            boolean serverTypeMatch = (queue.getServerType() == null && serverType == null) || (queue.getServerType() != null && queue.getServerType().equals(serverType));
            boolean gameSizeMatch = (queue.getGameSize() == null && gameSize == null) || (queue.getGameSize() != null && queue.getGameSize().equals(gameSize));

            if (serverMatch && gameMatch && serverTypeMatch && gameSizeMatch) {
                System.out.println("Matching handler found: " + handler);
                return handler;
            }
        }

        System.out.println("No matching handler found.");
        return null;
    }

    public HeriaQueueHandler getHandler(UUID player){
        for (HeriaQueueHandler queue : this.queues) {
            if(queue.getQueue().getPlayers().contains(player)){
                return queue;
            }
        }

        return null;
    }

    public Set<HeriaQueueHandler> getHandlers() {
        System.out.println("Retrieving all queue handlers.");
        return queues;
    }
}