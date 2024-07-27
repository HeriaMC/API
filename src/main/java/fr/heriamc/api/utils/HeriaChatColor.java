package fr.heriamc.api.utils;

public enum HeriaChatColor {

    BLACK('0'),
    DARK_BLUE('1'),
    DARK_GREEN('2'),
    DARK_AQUA('3'),
    DARK_RED('4'),
    DARK_PURPLE('5'),
    GOLD('6'),
    GRAY('7'),
    DARK_GRAY('8'),
    BLUE('9'),
    GREEN('a'),
    AQUA('b'),
    RED('c'),
    LIGHT_PURPLE('d'),
    YELLOW('e'),
    WHITE('f'),
    MAGIC('k'),
    BOLD('l'),
    STRIKETHROUGH('m'),
    UNDERLINE('n'),
    ITALIC('o'),
    RESET('r')

    ;

    private final static String CONVERTOR = "§";
    private final char plain;

    HeriaChatColor(char plain) {
        this.plain = plain;
    }

    public String getColor(){
        return CONVERTOR + this.plain;
    }

    @Override
    public String toString() {
        return getColor();
    }
}
