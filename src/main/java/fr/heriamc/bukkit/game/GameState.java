package fr.heriamc.bukkit.game;

public enum GameState {

    LOADING,
    LOADING_IN_PROGRESS,

    WAIT,
    ALWAYS_PLAYING,
    STARTING,
    IN_GAME,
    END;

    public boolean is(GameState state) {
        return this == state;
    }

    public boolean is(GameState... states) {
        for (GameState state : states)
            if (this == state) return true;

        return false;
    }

}