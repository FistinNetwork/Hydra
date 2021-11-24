package fr.fistin.hydra.api.redis.receiver;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 20/11/2021 at 10:24
 */
public interface IHydraChannelReceiver {

    /**
     * Called when a message is received on Redis PubSub
     *
     * @param channel - Received message channel
     * @param message - Received message
     */
    void receive(String channel, String message);

}
