package fr.heriamc.proxy.pool.types;

import fr.heriamc.api.messaging.packet.HeriaPacket;
import fr.heriamc.api.messaging.packet.HeriaPacketReceiver;
import fr.heriamc.api.server.HeriaServerType;
import fr.heriamc.api.game.HeriaGamesList;
import fr.heriamc.api.game.packet.GameCreatedPacket;
import fr.heriamc.api.game.packet.GameCreationRequestPacket;
import fr.heriamc.api.game.packet.GameCreationResult;
import fr.heriamc.api.game.packet.GameJoinPacket;
import fr.heriamc.api.game.size.GameSize;
import fr.heriamc.proxy.HeriaProxy;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class GameServerPool extends ServerPool implements HeriaPacketReceiver {

    private final GameSize gameSize;

    private boolean creatingGames = true;
    private boolean waitingServer;

    private List<String> lastCreated;
    private UUID lastRequestID;

    private int gamesToMake;
    private int remainingPlaces;

    public GameServerPool(HeriaProxy proxy, HeriaServerType serverType, GameSize gameSize) {
        super(proxy, serverType);
        this.gameSize = gameSize;
        this.remainingPlaces = gameSize.getMaxPlayer();
    }

    @Override
    public void run(){
        proxy.getServer().getScheduler().buildTask(proxy, (scheduledTask -> {
            if(gamesToMake == 0){
                return;
            }

            if(waitingServer && isServerEnabled){
                this.waitingServer = false;
                this.creatingGames = true;
            }

            createGame();
        })).repeat(500, TimeUnit.MILLISECONDS).schedule();
    }

    @Override
    public void createForPlayers(int players){
        int maxPlayer = gameSize.getMaxPlayer();
        int result = (players / maxPlayer) + 1;

        this.gamesToMake += result;
    }

    public void createGame(){
        if(lastServer == null){
            createServer();
            this.waitingServer = true;
        }

        if(waitingServer){
            return;
        }

        if(!creatingGames){
            return;
        }

        this.lastRequestID = UUID.randomUUID();
        this.proxy.getApi().getMessaging().send(new GameCreationRequestPacket(this.lastRequestID, lastServer, serverType, gameSize));
        this.creatingGames = false;
    }

    @Override
    public void execute(String channel, HeriaPacket packet) {
        if(!(packet instanceof GameCreatedPacket gamePacket)){
            return;
        }

        if(!gamePacket.getRequestID().equals(lastRequestID)){
            return;
        }

        GameCreationResult result = gamePacket.getResult();

        if(result == GameCreationResult.FAIL){
            this.createServer();
            this.waitingServer = true;

            return;
        }


        this.creatingGames = true;
        this.lastCreated.add(gamePacket.getGameName());
        this.gamesToMake -= 1;
    }

    @Override
    public boolean isAvailable() {
        return !this.lastCreated.isEmpty();
    }

    @Override
    public List<HeriaPacket> createPackets(UUID player) {
        List<HeriaPacket> packets = new ArrayList<>();
        packets.add(new GameJoinPacket(player, lastCreated.get(0), false));
        packets.addAll(super.createPackets(player));

        if(!isOldCorrect()){
            lastCreated.remove(0);
        }

        return packets;
    }

    private boolean isOldCorrect(){
        //HeriaGamesList heriaGamesList = proxy.getApi().getga
        return false;
    }
}
