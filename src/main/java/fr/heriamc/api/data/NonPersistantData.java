package fr.starblade.api.data;

public @interface NonPersistantData {

    boolean value() default true;

}
