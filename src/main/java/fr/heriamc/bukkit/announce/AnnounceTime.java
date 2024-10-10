package fr.heriamc.bukkit.announce;

public enum AnnounceTime {

    ONE_HOUR(0),
    THREE_HOURS(0),
    SIX_HOURS(0.50F),
    TWELVE_HOURS(1F),
    ONE_DAY(1.50F),
    TWO_DAYS(2F);

    private final float price;

    AnnounceTime(float price) {
        this.price = price;
    }
}
