package fr.heriamc.api.user.unlock;

import fr.heriamc.api.data.SerializableData;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

public class HeriaUnlockable implements SerializableData<UUID> {

    private UUID id;
    private Map<Serializable, Boolean> unlockableData;

    public HeriaUnlockable(UUID id, Map<Serializable, Boolean> unlockableData) {
        this.id = id;
        this.unlockableData = unlockableData;
    }

    public UUID getId() {
        return id;
    }

    public boolean isUnlocked(Serializable serializable){
        return this.unlockableData.containsKey(serializable);
    }

    public HeriaUnlockable unlock(Serializable serializable){
        this.unlockableData.put(serializable, true);
        return this;
    }

    public Map<Serializable, Boolean> getUnlockableData() {
        return unlockableData;
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
