package fr.fistin.hydra.api.redis;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 20/11/2021 at 09:48
 */
public class HydraPubSubMessage {

    /** Channel used to send the message */
    private final String channel;
    /** Message to send */
    private final String message;
    /** Callback to fire after sending message */
    private final Runnable callback;

    /**
     * Constructor of {@link HydraPubSubMessage}
     *
     * @param channel Channel
     * @param message Message to send
     * @param callback Callback
     */
    public HydraPubSubMessage(String channel, String message, Runnable callback) {
        this.channel = channel;
        this.message = message;
        this.callback = callback;
    }

    /**
     * Constructor of {@link HydraPubSubMessage}
     *
     * @param channel Channel
     * @param message Message to send
     */
    public HydraPubSubMessage(String channel, String message) {
        this(channel, message, null);
    }

    /**
     * Get channel
     *
     * @return Channel
     */
    public String getChannel() {
        return this.channel;
    }

    /**
     * Get message
     *
     * @return Message
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Get callback
     *
     * @return Callback
     */
    public Runnable getCallback() {
        return this.callback;
    }

}
