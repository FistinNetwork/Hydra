package fr.fistin.hydra.api.redis;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 28/12/2021 at 17:09
 */
@FunctionalInterface
public interface IHydraReceiver {

    /**
     * This method is fired when a message is received on the wanted channel
     *
     * @param channel The wanted channel
     * @param message The message received
     */
    void receive(String channel, String message);

}
