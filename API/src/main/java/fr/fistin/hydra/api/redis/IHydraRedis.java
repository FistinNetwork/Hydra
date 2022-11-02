package fr.fistin.hydra.api.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by AstFaster
 * on 02/11/2022 at 09:16
 */
public interface IHydraRedis {

    /**
     * Get the Redis pool.
     *
     * @return The {@link JedisPool} instance
     */
    JedisPool getPool();

    /**
     * Get a resource from the Redis pool.
     *
     * @return A {@link Jedis} instance
     */
    default Jedis getResource() {
        return this.getPool().getResource();
    }

    /**
     * Process an action on Redis.
     *
     * @param jedisConsumer The action to process
     */
    default void process(Consumer<Jedis> jedisConsumer) {
        try (final Jedis jedis = this.getResource()) {
            if (jedis != null) {
                jedisConsumer.accept(jedis);
            }
        }
    }

    /**
     * Process an action on Redis but with a result.
     *
     * @param jedisFunction The action to process
     * @return The result of the action
     * @param <T> The type of the result
     */
    default <T> T get(Function<Jedis, T> jedisFunction) {
        try (final Jedis jedis = this.getResource()) {
            if (jedis != null) {
                return jedisFunction.apply(jedis);
            }
        }
        return null;
    }

}
