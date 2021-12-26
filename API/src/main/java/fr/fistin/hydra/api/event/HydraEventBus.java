package fr.fistin.hydra.api.event;

import fr.fistin.hydra.api.HydraAPI;
import fr.fistin.hydra.api.protocol.HydraChannel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 24/11/2021 at 13:51
 */
public class HydraEventBus {

    /** Encoded event message split char */
    private static final String SPLIT_CHAR = "&";

    /** List of all {@link HydraEventContext} objects */
    private final List<HydraEventContext<?>> contexts;

    /** {@link HydraAPI} instance */
    private final HydraAPI hydraAPI;

    /**
     * Constructor of {@link HydraEventBus}
     *
     * @param hydraAPI {@link HydraAPI} instance
     */
    public HydraEventBus(HydraAPI hydraAPI) {
        this.hydraAPI = hydraAPI;
        this.contexts = new ArrayList<>();
    }

    /**
     * Start the event bus by subscribing on events channel
     */
    public void start() {
        HydraAPI.log("Starting event bus...");

        this.hydraAPI.getPubSub().subscribe(HydraChannel.EVENTS, (channel, message) -> {
            final HydraEvent event = this.decode(message);

            if (event != null) {
                for (HydraEventContext<?> context : this.contexts) {
                    // Check if the event type is the same as the received one
                    if (context.getEventType().isAssignableFrom(event.getClass())) {
                        try {
                            // Invoke onEvent method
                            final Method method = context.getEventListener().getClass().getMethod("onEvent", HydraEvent.class);

                            method.setAccessible(true);
                            method.invoke(context.getEventListener(), event);
                        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    /**
     * Subscribe a listener for an event type
     *
     * @param eventClass Event type to listen
     * @param eventListener Listener to fire
     * @param <E> Type
     */
    public <E extends HydraEvent> void subscribe(Class<E> eventClass, HydraEventListener<E> eventListener) {
        this.contexts.add(new HydraEventContext<>(eventClass, eventListener));
    }

    /**
     * Publish a given event
     *
     * @param event Event to publish
     * @param <E> Type
     */
    public <E extends HydraEvent> void publish(E event) {
        this.hydraAPI.getPubSub().send(HydraChannel.EVENTS, this.encode(event));
    }

    /**
     * Encode an event before sending it on PubSub
     *
     * @param event Event to encode
     * @return Encoded event
     */
    private String encode(HydraEvent event) {
        return event.getClass().getName() + SPLIT_CHAR + HydraAPI.GSON.toJson(event);
    }

    /**
     * Decode an event from a received message
     *
     * @param message Message to decode
     * @return Decoded event
     */
    private HydraEvent decode(String message) {
        try {
            final String[] splitedRaw  = message.split(SPLIT_CHAR);
            final Class<?> clazz = Class.forName(splitedRaw[0]);
            final String json = splitedRaw[1];

            return (HydraEvent) HydraAPI.GSON.fromJson(json, clazz);
        } catch (Exception e) {
            return null;
        }
    }

}
