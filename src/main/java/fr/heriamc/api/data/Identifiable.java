package fr.starblade.api.data;

public interface Identifiable<A> {

    A getIdentifier();
    void setIdentifier(A identifier);

}
