package fr.heriamc.api.user.rank;


import fr.heriamc.api.utils.HeriaChatColor;

public enum HeriaRank {

    OWNER("Gérant", HeriaChatColor.DARK_RED, 100, 1),
    ADMIN("Admin", HeriaChatColor.RED, 90, 2),
    RESPONSABLE("Responsable", HeriaChatColor.RED, 80, 3),
    SUPER_MOD("SuperModo", HeriaChatColor.RED, 70, 4),
    MOD("Modérateur", HeriaChatColor.BLUE, 60, 5),
    HELPER("Assistant", HeriaChatColor.AQUA, 50, 6),
    DEV("Développeur", HeriaChatColor.DARK_GREEN, 1000, 7),
    BUILD("Builder", HeriaChatColor.GREEN, 40, 8),
    GRAPHIC("Graphiste", HeriaChatColor.DARK_GREEN, 40, 9),

    YOUTUBER("Youtuber", HeriaChatColor.GOLD, 8, 10),
    STREAMER("Streamer", HeriaChatColor.DARK_PURPLE, 7, 11),
    CUSTOM("Perso", HeriaChatColor.WHITE, 6, 12),
    SUPREME("Suprême", HeriaChatColor.LIGHT_PURPLE, 5, 13),
    SUPER_VIP("SuperVIP", HeriaChatColor.AQUA, 4, 14),
    VIP_PLUS("VIP+", HeriaChatColor.DARK_AQUA, 3, 15),
    VIP("VIP", HeriaChatColor.YELLOW, 2, 16),
    PLAYER("Joueur", HeriaChatColor.GRAY, 1, 17),

    ;

    public final static HeriaRank DEFAULT = PLAYER;

    private final String name;
    private final HeriaChatColor color;
    private final int power;
    private final int tabPriority;

    HeriaRank(String name, HeriaChatColor color, int power, int tabPriority) {
        this.name = name;
        this.color = color;
        this.power = power;
        this.tabPriority = tabPriority;
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

    public int getTabPriority() {
        return tabPriority;
    }
}
