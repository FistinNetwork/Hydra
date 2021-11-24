package fr.fistin.hydra.api.redis.receiver;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 20/11/2021 at 10:24
 */
public interface IHydraPatternReceiver {

    /**
     * Called when a message is received on Redis PubSub
     *
     * @param pattern - Received message pattern
     * @param channel - Received message channel
     * @param message - Received message
     */
    void receive(String pattern, String channel, String message);

}
