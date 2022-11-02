package fr.fistin.hydra.redis;

import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.api.protocol.data.RedisData;
import fr.fistin.hydra.api.redis.IHydraRedis;
import fr.fistin.hydra.util.References;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 25/10/2021 at 09:51
 */
public class HydraRedis implements IHydraRedis {

    private JedisPool jedisPool;

    private Thread reconnectTask;

    private boolean connected;

    private final String hostname;
    private final int port;
    private final String password;

    public HydraRedis() {
        final RedisData redisData = Hydra.get().getConfig().getRedis();

        this.hostname = redisData.getHostname();
        this.port = redisData.getPort();
        this.password = redisData.getPassword();
    }

    public boolean connect() {
        final JedisPoolConfig config = new JedisPoolConfig();

        config.setJmxEnabled(false);
        config.setMaxTotal(-1);

        if (this.password != null && !this.password.isEmpty()) {
            this.jedisPool = new JedisPool(config, this.hostname, this.port, 2000, this.password);
        } else {
            this.jedisPool = new JedisPool(config, this.hostname, this.port, 2000);
        }

        try {
            this.getResource().close();

            this.connected = true;

            System.out.println(References.NAME + " is now connected with Redis database.");

            this.startReconnectTask();

            return true;
        } catch (Exception e) {
            System.err.println("Couldn't connect to Redis database!");
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
            this.getResource().close();
        } catch (Exception e) {
            System.err.println("Encountered exception in Redis reconnection task. Error: " + e.getMessage());
            System.err.println("Error in Redis database connection ! Trying to reconnect...");

            this.connected = false;

            this.connect();
        }
    }

    public void disconnect() {
        System.out.println("Disconnecting " + References.NAME + " from Redis database...");

        if (this.reconnectTask != null && this.reconnectTask.isAlive()) {
            this.reconnectTask.interrupt();
        }

        this.connected = false;

        this.jedisPool.close();
    }

    @Override
    public JedisPool getPool() {
        return this.jedisPool;
    }

    public boolean isConnected() {
        return this.connected;
    }

}
