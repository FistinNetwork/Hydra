package fr.fistin.hydra.api;

import fr.fistin.hydra.api.protocol.codec.HydraPacketDecoder;
import fr.fistin.hydra.api.protocol.codec.HydraPacketEncoder;
import fr.fistin.hydra.api.protocol.codec.IHydraPacketDecoder;
import fr.fistin.hydra.api.protocol.codec.IHydraPacketEncoder;
import redis.clients.jedis.JedisPool;

import java.util.logging.Logger;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 20/11/2021 at 09:36
 */
public interface IHydraProvider {

    /**
     * Get {@link Logger} object to print info
     *
     * @return - {@link Logger} object
     */
    Logger getLogger();

    /**
     * Get an instance of a Redis pool
     *
     * @return - {@link JedisPool} instance
     */
    JedisPool getJedisPool();

    /**
     * Get packet encoder instance.<br>
     * Override to change the default encoder
     *
     * @return {@link IHydraPacketEncoder} instance
     */
    default IHydraPacketEncoder getPacketEncoder() {
        return new HydraPacketEncoder();
    }

    /**
     * Get packet decoder instance.<br>
     * Override to change the default decoder
     *
     * @return {@link IHydraPacketDecoder} instance
     */
    default IHydraPacketDecoder getPacketDecoder() {
        return new HydraPacketDecoder();
    }

}
