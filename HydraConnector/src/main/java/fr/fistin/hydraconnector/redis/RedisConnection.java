package fr.fistin.hydraconnector.redis;

import fr.fistin.hydraconnector.HydraConnector;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisConnection {

    private JedisPool jedisPool;
    private Thread reconnectTask;

    private final HydraConnector hydraConnector;
    private final String redisHost;
    private final int redisPort;
    private final String redisPass;

    public RedisConnection(HydraConnector hydraConnector, String redisHost, int redisPort, String redisPass) {
        this.hydraConnector = hydraConnector;
        this.redisHost = redisHost;
        this.redisPort = redisPort;
        this.redisPass = redisPass;
    }

    public boolean connect() {
        this.hydraConnector.log("Connecting to redis...");

        final JedisPoolConfig config = new JedisPoolConfig();
        config.setJmxEnabled(false);
        config.setMaxTotal(-1);

        this.jedisPool = new JedisPool(config, this.redisHost, this.redisPort, 0, this.redisPass);

        try {
            this.jedisPool.getResource().close();
            this.hydraConnector.log("Connected to redis.");
            return true;
        } catch (Exception e) {
            this.hydraConnector.log("Couldn't connect to redis !");
            return false;
        }
    }

    public void startReconnectTask() {
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

    public void reconnect() {
        try {
            this.jedisPool.getResource().close();
        } catch (Exception e) {
            this.hydraConnector.log(String.format("[%s] Encountered exception in reconnection task. Error: %s", "ERROR", e.getMessage()));
            this.hydraConnector.log(String.format("[%s] Error in redis connection ! Trying to reconnect...", "ERROR"));
            this.connect();
        }
    }

    public void disconnect() {
        this.hydraConnector.log("Disconnecting from redis...");
        if (this.reconnectTask != null && this.reconnectTask.isAlive()) this.reconnectTask.interrupt();
        this.jedisPool.close();
    }

    public JedisPool getJedisPool() {
        return this.jedisPool;
    }

    public Jedis getJedis() {
        return this.jedisPool.getResource();
    }

}
