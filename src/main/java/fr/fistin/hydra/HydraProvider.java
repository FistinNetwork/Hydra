package fr.fistin.hydra;

import fr.fistin.hydra.api.IHydraProvider;
import redis.clients.jedis.JedisPool;

import java.util.logging.Logger;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 24/11/2021 at 20:06
 */
public record HydraProvider(Hydra hydra) implements IHydraProvider {

    @Override
    public Logger getLogger() {
        return this.hydra.getLogger();
    }

    @Override
    public JedisPool getJedisPool() {
        return this.hydra.getRedisConnection().getJedisPool();
    }

}
