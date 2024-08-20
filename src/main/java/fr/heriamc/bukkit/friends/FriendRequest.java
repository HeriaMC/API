package fr.heriamc.bukkit.friends;

import fr.heriamc.api.data.SerializableData;

import java.util.UUID;

public class FriendRequest implements SerializableData<UUID> {

    private UUID id;
    private UUID sender;
    private UUID receiver;
    private long instant;

    public FriendRequest(UUID id, UUID sender, UUID receiver, long instant) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.instant = instant;
    }

    public UUID getId() {
        return id;
    }

    public FriendRequest setId(UUID id) {
        this.id = id;
        return this;
    }

    public UUID getSender() {
        return sender;
    }

    public FriendRequest setSender(UUID sender) {
        this.sender = sender;
        return this;
    }

    public UUID getReceiver() {
        return receiver;
    }

    public FriendRequest setReceiver(UUID receiver) {
        this.receiver = receiver;
        return this;
    }

    public long getInstant() {
        return instant;
    }

    public FriendRequest setInstant(long instant) {
        this.instant = instant;
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
