package fr.heriamc.api.server;

public enum HeriaServerType {

    HUB("hub"),
    HIKABRAIN("hikabrain");

    private final String name;


    HeriaServerType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
