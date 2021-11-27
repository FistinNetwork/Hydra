package fr.fistin.hydra.api.util;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 26/11/2021 at 07:21
 */
public class Pair<U, V> {

    /** The first element */
    private U first;

    /** The second element */
    private V second;

    /**
     * Constructor of {@link Pair}
     *
     * @param first  The first element
     * @param second The second element
     */
    public Pair(U first, V second) {
        this.first = first;
        this.second = second;
    }

    public U getFirst() {
        return this.first;
    }

    public void setFirst(U first) {
        this.first = first;
    }

    public V getSecond() {
        return this.second;
    }

    public void setSecond(V second) {
        this.second = second;
    }

}
