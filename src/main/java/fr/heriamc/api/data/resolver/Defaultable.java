package fr.starblade.api.data.resolver;

import fr.starblade.api.data.SerializableData;

public interface Defaultable<A extends SerializableData> {

    A getDefault();

}
