package fr.fistin.hydra.redis;

import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.configuration.HydraConfiguration;
import fr.fistin.hydra.util.References;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 25/10/2021 at 09:51
 */
public class RedisConnection {

    private JedisPool jedisPool;

    private Thread reconnectTask;

    private final String redisHost;
    private final int redisPort;
    private final String redisPass;

    public RedisConnection(HydraConfiguration configuration) {
        this.redisHost = configuration.getRedisIp();
        this.redisPort = configuration.getRedisPort();
        this.redisPass = configuration.getRedisPassword();
    }

    public boolean connect() {
        final JedisPoolConfig config = new JedisPoolConfig();

        config.setJmxEnabled(false);
        config.setMaxTotal(-1);

        this.jedisPool = new JedisPool(config, this.redisHost, this.redisPort, 0, this.redisPass);

        try {
            this.jedisPool.getResource().close();

            System.out.println(References.HYDRA + " is now connected to Redis database!");

            this.startReconnectTask();

            return true;
        } catch (Exception e) {
            System.err.println("Couldn't connect to Redis database !");
            return false;
        }
    }

    private void startReconnectTask() {
        this.reconnectTask = new Thread(() -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            while (!Thread.interrupted()) {
                this.reconnect();

                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        this.reconnectTask.start();
    }

    private void reconnect() {
        try {
            this.jedisPool.getResource().close();
        } catch (Exception e) {
            System.err.println("Encountered exception in Redis reconnection task. Error:" + e.getMessage());
            System.err.println("Error in Redis database connection ! Trying to reconnect...");

            this.connect();
        }
    }

    public void disconnect() {
        System.out.println("Disconnecting " + References.HYDRA + " from Redis database...");

        if (this.reconnectTask != null && this.reconnectTask.isAlive()) {
            this.reconnectTask.interrupt();
        }

        this.jedisPool.close();
    }

    public JedisPool getJedisPool() {
        return this.jedisPool;
    }

    public Jedis getJedis() {
        return this.jedisPool.getResource();
    }

}
