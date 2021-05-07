package fr.fistin.hydraconnector;

import com.google.gson.Gson;
import fr.fistin.hydraconnector.protocol.HydraConnectionManager;
import fr.fistin.hydraconnector.redis.RedisConnection;
import fr.fistin.hydraconnector.redis.RedisHandler;
import fr.fistin.hydraconnector.util.References;

public class HydraConnector {

    private RedisHandler redisHandler;

    private final RedisConnection redisConnection;
    private final HydraConnectionManager connectionManager;

    private final Gson gson;

    public HydraConnector(Gson gson, String redisHost, int redisPort, String redisPass) {
        this.gson = gson;
        this.redisConnection = new RedisConnection(this, redisHost, redisPort, redisPass);
        this.connectionManager = new HydraConnectionManager(this);
    }


    public boolean connectToRedis() {
        if (this.redisConnection.connect()) {
            this.redisConnection.startReconnectTask();
            return true;
        }
        return false;
    }

    public void startPacketHandler() {
        this.redisHandler = new RedisHandler(this);
        this.redisHandler.subscribe();
    }

    public void log(String message) {
        System.out.println("[" + References.NAME + "] " + message);
    }

    public HydraConnectionManager getConnectionManager() {
        return this.connectionManager;
    }

    public RedisConnection getRedisConnection() {
        return this.redisConnection;
    }

    public RedisHandler getRedisHandler() {
        return this.redisHandler;
    }

    public Gson getGson() {
        return this.gson;
    }
}
