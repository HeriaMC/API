package fr.heriamc.api.utils;

public enum HeriaSkull {

    LIME("f9b43f813dc1f8c853454c45f1ede94f43873df4742cd90d18ec842fafb8878d")

    ;

    private final String data;

    HeriaSkull(String data) {
        this.data = data;
    }

    public String getURL(){
        return "http://textures.minecraft.net/texture/" + data;
    }
}
