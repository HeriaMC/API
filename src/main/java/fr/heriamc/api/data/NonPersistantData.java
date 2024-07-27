package fr.heriamc.api.data;

public @interface NonPersistantData {

    boolean value() default true;

}
