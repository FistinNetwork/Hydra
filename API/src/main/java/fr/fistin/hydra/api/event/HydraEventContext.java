package fr.fistin.hydra.api.event;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 24/11/2021 at 14:24
 */
public class HydraEventContext<E extends HydraEvent> {

    /** Event's type */
    private final Class<E> eventType;

    /** Event's listener */
    private final HydraEventListener<E> eventListener;

    /**
     * Constructor of {@link HydraEventContext}
     *
     * @param eventType Type
     * @param eventListener Listener
     */
    public HydraEventContext(Class<E> eventType, HydraEventListener<E> eventListener) {
        this.eventType = eventType;
        this.eventListener = eventListener;
    }

    /**
     * Get event's type
     *
     * @return A {@link Class}
     */
    public Class<E> getEventType() {
        return this.eventType;
    }

    /**
     * Get event's listener
     *
     * @return {@link HydraEventListener} object
     */
    public HydraEventListener<E> getEventListener() {
        return this.eventListener;
    }

}
