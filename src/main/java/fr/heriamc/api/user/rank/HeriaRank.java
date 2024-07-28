package fr.heriamc.api.user.rank;


import fr.heriamc.api.utils.HeriaChatColor;

public enum HeriaRank {

    OWNER("Gérant", HeriaChatColor.DARK_RED, 100),
    ADMIN("Admin", HeriaChatColor.RED, 90),
    RESPONSABLE("Responsable", HeriaChatColor.RED, 80),
    SUPER_MOD("SuperModo", HeriaChatColor.RED, 70),
    MOD("Modérateur", HeriaChatColor.BLUE, 60),
    HELPER("Assistant", HeriaChatColor.AQUA, 50),
    DEV("Développeur", HeriaChatColor.DARK_GREEN, 40),
    BUILD("Builder", HeriaChatColor.GREEN, 40),
    GRAPHIC("Graphiste", HeriaChatColor.DARK_GREEN, 40),

    YOUTUBER("Youtuber", HeriaChatColor.GOLD, 8),
    STREAMER("Streamer", HeriaChatColor.DARK_PURPLE, 7),
    CUSTOM("Perso", HeriaChatColor.WHITE, 6),
    SUPREME("Suprême", HeriaChatColor.LIGHT_PURPLE, 5),
    SUPER_VIP("SuperVIP", HeriaChatColor.AQUA, 4),
    VIP_PLUS("VIP+", HeriaChatColor.DARK_AQUA, 3),
    VIP("VIP", HeriaChatColor.YELLOW, 2),
    PLAYER("Joueur", HeriaChatColor.GRAY, 1),

    ;

    public final static HeriaRank DEFAULT = PLAYER;

    private final String name;
    private final HeriaChatColor color;
    private final int power;

    HeriaRank(String name, HeriaChatColor color, int power) {
        this.name = name;
        this.color = color;
        this.power = power;
    }

    public String getName() {
        return name;
    }

    public HeriaChatColor getColor() {
        return color;
    }

    public String getPrefix(){
        return getColor() + getName() + " " + getColor();
    }

    public int getPower() {
        return power;
    }
}
