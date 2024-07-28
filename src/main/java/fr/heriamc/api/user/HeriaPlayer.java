package fr.heriamc.api.user;

import fr.heriamc.api.data.NonPersistantData;
import fr.heriamc.api.data.SerializableData;
import fr.heriamc.api.user.rank.HeriaRank;

import java.util.UUID;

public class HeriaPlayer implements SerializableData<UUID> {

    private UUID id;
    private String name;
    private HeriaRank rank;

    @NonPersistantData
    private String connectedTo;

    public HeriaPlayer(UUID id, String name, HeriaRank rank, String connectedTo) {
        this.id = id;
        this.name = name;
        this.rank = rank;
        this.connectedTo = connectedTo;
    }

    public UUID getId() {
        return id;
    }

    public HeriaPlayer setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public HeriaPlayer setName(String name) {
        this.name = name;
        return this;
    }

    public HeriaRank getRank() {
        return rank;
    }

    public HeriaPlayer setRank(HeriaRank rank) {
        this.rank = rank;
        return this;
    }

    public String getConnectedTo() {
        return connectedTo;
    }

    public HeriaPlayer setConnectedTo(String connectedTo) {
        this.connectedTo = connectedTo;
        return this;
    }

    public boolean isConnected(){
        return this.connectedTo != null;
    }

    @Override
    public UUID getIdentifier() {
        return this.id;
    }

    @Override
    public void setIdentifier(UUID identifier) {
        this.id = identifier;
    }
}
