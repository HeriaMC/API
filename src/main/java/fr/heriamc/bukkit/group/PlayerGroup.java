package fr.heriamc.bukkit.group;

import fr.heriamc.api.data.SerializableData;

import java.util.List;
import java.util.UUID;

public class PlayerGroup implements SerializableData<UUID> {

    private UUID id;
    private UUID owner;
    private List<UUID> members;

    public PlayerGroup(UUID id, UUID owner, List<UUID> members) {
        this.id = id;
        this.owner = owner;
        this.members = members;
    }

    public UUID getId() {
        return id;
    }

    public PlayerGroup setId(UUID id) {
        this.id = id;
        return this;
    }

    public UUID getOwner() {
        return owner;
    }

    public PlayerGroup setOwner(UUID owner) {
        this.owner = owner;
        return this;
    }

    public List<UUID> getMembers() {
        return members;
    }

    public PlayerGroup setMembers(List<UUID> members) {
        this.members = members;
        return this;
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
