package fr.heriamc.api.server;

import fr.heriamc.api.utils.HeriaChatColor;
import fr.heriamc.api.utils.HeriaSkull;

public enum HeriaServerStatus {

    STARTING("Démarrage", (byte) 4, HeriaChatColor.GOLD, HeriaSkull.LIME, false, false),
    STARTED("Démarré", (byte) 5, HeriaChatColor.GREEN, HeriaSkull.LIME, true, false),
    WAITING_PLAYERS("En attente de joueurs", (byte) 5, HeriaChatColor.YELLOW, HeriaSkull.LIME, true, true),
    GAME_STARTING("Démarrage de la partie", (byte) 9, HeriaChatColor.GOLD, HeriaSkull.LIME, true, true),
    GAME_PLAYING("Partie en cours", (byte) 9, HeriaChatColor.RED, HeriaSkull.LIME, true, true),
    GAME_ENDING("Fin de la partie", (byte) 15, HeriaChatColor.RED, HeriaSkull.LIME, true, false),
    STOPPING("Arrêt", (byte) 14, HeriaChatColor.RED, HeriaSkull.LIME, false, false);
    
    private final String name;
    private final byte byteColor;
    private final HeriaChatColor color;
    private final HeriaSkull skull;
    private final boolean reachable;
    private final boolean gameReachable;
    
    HeriaServerStatus(String name, byte byteColor, HeriaChatColor color, HeriaSkull skull, boolean reachable, boolean gameReachable) {
        this.name = name;
        this.byteColor = byteColor;
        this.color = color;
        this.skull = skull;
        this.reachable = reachable;
        this.gameReachable = gameReachable;
    }

    public String getName() {
        return name;
    }

    public byte getByteColor() {
        return byteColor;
    }

    public HeriaChatColor getColor() {
        return color;
    }

    public HeriaSkull getSkull() {
        return skull;
    }

    public boolean isReachable() {
        return reachable;
    }

    public boolean isGameReachable() {
        return gameReachable;
    }
}
