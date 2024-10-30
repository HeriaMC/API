package fr.heriamc.proxy.queue;

import fr.heriamc.api.HeriaAPI;
import fr.heriamc.api.queue.HeriaQueue;
import fr.heriamc.api.server.HeriaServerType;
import fr.heriamc.api.user.HeriaPlayer;
import fr.heriamc.api.game.size.GameSize;
import fr.heriamc.proxy.HeriaProxy;
import fr.heriamc.proxy.pool.HeriaPool;

import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

public class HeriaQueueHandler {

    private final PriorityBlockingQueue<Set<UUID>> groupsQueue = new PriorityBlockingQueue<>(1000, Comparator.comparing(this::calculatePriority));
    private long lastProcess;

    private final HeriaQueue queue;
    private HeriaPool pool;

    public HeriaQueueHandler(String server){
        this(new HeriaQueue(UUID.randomUUID(), HeriaQueue.QueueType.SERVER, null, null, new HashSet<>()));
    }

    public HeriaQueueHandler(String server, String gameName){
        this(new HeriaQueue(UUID.randomUUID(), HeriaQueue.QueueType.GAME, server, gameName, new HashSet<>()));
    }

    public HeriaQueueHandler(HeriaServerType serverType){
        this(new HeriaQueue(UUID.randomUUID(), HeriaQueue.QueueType.SERVER, null, null, new HashSet<>()));
        this.pool = HeriaProxy.get().getPoolManager().getServerPool(serverType);
    }

    public HeriaQueueHandler(HeriaServerType serverType, GameSize gameSize){
        this(new HeriaQueue(UUID.randomUUID(), HeriaQueue.QueueType.GAME, null, null, new HashSet<>()));
        this.pool = HeriaProxy.get().getPoolManager().getGamePool(serverType, gameSize);
    }

    public HeriaQueueHandler(HeriaQueue queue) {
        this.queue = queue;
        this.pool = null;
    }

    public void disable(){

    }

    public void processPlayers(){
        this.groupsQueue.addAll(this.queue.getTotalPlayers());

        //kill queue if empty more than a minute
        if(this.groupsQueue.size() == 0 & System.currentTimeMillis() - this.lastProcess >= TimeUnit.MINUTES.toMillis(1)){
            this.disable();
        }

        if(this.queue.getType() == HeriaQueue.QueueType.GAME){
            this.processGame();
        } else if(this.queue.getType() == HeriaQueue.QueueType.SERVER) {
            this.processServer();
        }

        if(this.groupsQueue.size() > 0){
            this.lastProcess = System.currentTimeMillis();
        }

        this.groupsQueue.clear();
    }

    public void processGame(){

    }

    public void processServer(){

    }

    private int calculatePriority(Set<UUID> group) {
        int priority = -1;

        for (UUID member : group) {
            HeriaPlayer account = HeriaAPI.get().getPlayerManager().get(member);

            if (priority == -1 || account.getRank().getTabPriority() < priority) {
                priority = account.getRank().getTabPriority();
            }
        }
        return priority;
    }

}
