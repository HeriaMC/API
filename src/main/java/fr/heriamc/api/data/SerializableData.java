package fr.starblade.api.data;

import fr.starblade.api.utils.GsonUtils;

public interface SerializableData<A> extends Identifiable<A> {

    default String toJson(){
        return GsonUtils.get().toJson(this);
    }

    static <T extends SerializableData<?>> T fromJson(String json, Class<T> typeOfT){
        return GsonUtils.get().fromJson(json, typeOfT);
    }

}