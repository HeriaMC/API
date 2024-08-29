package fr.heriamc.api.utils;

public enum HeriaSkull {

    LIME("f9b43f813dc1f8c853454c45f1ede94f43873df4742cd90d18ec842fafb8878d"),
    YELLOW("3da29fe23b69f76bc49474102226b0699ca2a5ad11f7a48542b13ad9cabaf89d"),
    RED("c3a03c06ffe2356ce00aef5b708878d2fe4365a97bc4dae1e1542c27b2eb30dd"),
    ORANGE("ee30480ed2834f5b7cc81dcb1dc7e4766792430c49dad4dbcd10a93c5e1457bc"),

    GRAY_BACKWARDS("74133f6ac3be2e2499a784efadcfffeb9ace025c3646ada67f3414e5ef3394"),
    GRAY_FORWARD("e02fa3b2dcb11c6639cc9b9146bea54fbc6646d855bdde1dc6435124a11215d"),

    GREEN_PLUS("5ff31431d64587ff6ef98c0675810681f8c13bf96f51d9cb07ed7852b2ffd1"),
    RED_MINUS("4e4b8b8d2362c864e062301487d94d3272a6b570afbf80c2c5b148c954579d46"),

    ;

    private final String data;

    HeriaSkull(String data) {
        this.data = data;
    }

    public String getURL(){
        return "http://textures.minecraft.net/texture/" + data;
    }
}
