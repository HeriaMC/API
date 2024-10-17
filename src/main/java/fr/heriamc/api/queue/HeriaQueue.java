package fr.heriamc.api.queue;

import fr.heriamc.api.HeriaAPI;
import fr.heriamc.api.user.HeriaPlayer;
import fr.heriamc.bukkit.HeriaBukkit;

import java.util.Set;
import java.util.UUID;

public class HeriaQueue {

    private UUID id;
    private QueueType type;

    private String server;
    private String game;

    private Set<UUID> players;

    public HeriaQueue(UUID id, QueueType type, String server, String game, Set<UUID> players) {
        this.id = id;
        this.type = type;
        this.server = server;
        this.game = game;
        this.players = players;
    }


    public UUID getId() {
        return id;
    }

    public HeriaQueue setId(UUID id) {
        this.id = id;
        return this;
    }

    public QueueType getType() {
        return type;
    }

    public HeriaQueue setType(QueueType type) {
        this.type = type;
        return this;
    }

    public String getServer() {
        return server;
    }

    public HeriaQueue setServer(String server) {
        this.server = server;
        return this;
    }

    public String getGame() {
        return game;
    }

    public HeriaQueue setGame(String game) {
        this.game = game;
        return this;
    }

    public Set<UUID> getPlayers() {
        return players;
    }

    public HeriaQueue setPlayers(Set<UUID> players) {
        this.players = players;
        return this;
    }

    public Set<Set<UUID>> getTotalPlayers(){
        return null;
    }

    public enum QueueType {

        GAME,
        SERVER

        ;

    }
}
