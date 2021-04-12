package fr.fistin.hydra.redis;

import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.scheduler.HydraTask;
import fr.fistin.hydra.util.logger.LogType;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.concurrent.TimeUnit;

public class RedisConnector {

    private HydraTask reconnectTask;
    private JedisPool jedisPool;

    private final Hydra hydra;

    public RedisConnector(Hydra hydra) {
        this.hydra = hydra;
    }

    public void connect() {
        this.hydra.getLogger().log(LogType.INFO, "Connecting to redis...");

        final JedisPoolConfig config = new JedisPoolConfig();
        config.setJmxEnabled(false);
        config.setMaxTotal(-1);

        this.jedisPool = new JedisPool(config, this.hydra.getConfiguration().getRedisIp(), this.hydra.getConfiguration().getRedisPort(), 0, this.hydra.getConfiguration().getRedisPassword());

        try {
            this.jedisPool.getResource().close();
            this.hydra.getLogger().log(LogType.INFO, "Connected to redis.");
        } catch (Exception e) {
            this.hydra.getLogger().log(LogType.ERROR, "Couldn't connect to redis !");
            this.hydra.shutdown();
        }
    }

    public void startReconnectTask() {
        this.reconnectTask = this.hydra.getScheduler().schedule(this::reconnect, 10, 10, TimeUnit.SECONDS);
    }

    public void reconnect() {
        try {
            this.jedisPool.getResource().close();
        } catch (Exception e) {
            this.hydra.getLogger().log(LogType.ERROR, String.format("Encountered exception in reconnection task. Error: %s", e.getMessage()));
            this.hydra.getLogger().log(LogType.ERROR, "Error in redis connection ! Trying to reconnect...");
            this.connect();
        }
    }

    public void disconnect() {
        this.hydra.getLogger().log(LogType.INFO, "Disconnecting from redis...");
        if (this.reconnectTask != null && this.reconnectTask.isRunning()) this.reconnectTask.cancel();
        this.jedisPool.close();
    }

    public JedisPool getJedisPool() {
        return this.jedisPool;
    }

    public Jedis getResource() {
        return this.jedisPool.getResource();
    }

}
