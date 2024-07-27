package fr.heriamc.api.user.rank;


import fr.heriamc.api.utils.HeriaChatColor;

public enum HeriaRank {

    OWNER("Gérant", HeriaChatColor.DARK_RED),
    ADMIN("Admin", HeriaChatColor.RED),
    RESPONSABLE("Responsable", HeriaChatColor.RED),
    SUPER_MOD("SuperModo", HeriaChatColor.RED),
    MOD("Modérateur", HeriaChatColor.BLUE),
    HELPER("Assistant", HeriaChatColor.AQUA),
    DEV("Développeur", HeriaChatColor.DARK_GREEN),
    BUILD("Builder", HeriaChatColor.GREEN),
    GRAPHIC("Graphiste", HeriaChatColor.DARK_GREEN),

    YOUTUBER("Youtuber", HeriaChatColor.GOLD),
    STREAMER("Streamer", HeriaChatColor.DARK_PURPLE),
    CUSTOM("Perso", HeriaChatColor.WHITE),
    SUPREME("Suprême", HeriaChatColor.LIGHT_PURPLE),
    SUPER_VIP("SuperVIP", HeriaChatColor.AQUA),
    VIP_PLUS("VIP+", HeriaChatColor.DARK_AQUA),
    VIP("VIP", HeriaChatColor.YELLOW),
    PLAYER("Joueur", HeriaChatColor.GRAY),

    ;

    public final static HeriaRank DEFAULT = PLAYER;

    private final String name;
    private final HeriaChatColor color;

    HeriaRank(String name, HeriaChatColor color) {
        this.name = name;
        this.color = color;
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

    public int getPower(){
        return this.ordinal() + 1;
    }
}
