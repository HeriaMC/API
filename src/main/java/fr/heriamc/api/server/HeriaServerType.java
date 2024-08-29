package fr.heriamc.api.server;

public enum HeriaServerType {

    HUB("hub"),

    RUSHFFA("rushffa"),
    ONESHOT("oneshot"),

    HIKABRAIN("hikabrain"),
    FREECUBE("freecube"),
    JUMPSCADE("jumpscade")

    ;

    private final String name;


    HeriaServerType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
