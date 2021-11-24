package fr.fistin.hydra.api.event;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 24/11/2021 at 13:53
 */
@FunctionalInterface
public interface HydraEventListener<E extends HydraEvent> {

    /**
     * Called when the asked event is fired
     *
     * @param event Event fired
     */
    void onEvent(E event);

}
