package fr.fistin.hydra.api.redis;

import fr.fistin.hydra.api.HydraAPI;
import redis.clients.jedis.Jedis;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 20/11/2021 at 09:48
 */
class HydraPubSubPublisher implements Runnable {

    private boolean running = true;

    private Jedis jedis;

    private final LinkedBlockingQueue<HydraPubSubMessage> messages;

    private final HydraAPI hydraAPI;

    public HydraPubSubPublisher(HydraAPI hydraAPI) {
        this.hydraAPI = hydraAPI;
        this.messages = new LinkedBlockingQueue<>();
    }

    public void publish(HydraPubSubMessage message) {
        this.messages.add(message);
    }

    @Override
    public void run() {
        this.running = true;

        this.checkRedis();

        while (this.running) {
            try {
                this.processMessage(this.messages.take());
            } catch (InterruptedException e) {
                HydraAPI.log(Level.INFO, String.format("[%s] Interrupted!", this.getClass().getSimpleName()));
                this.jedis.close();
                return;
            }
        }
    }

    private void processMessage(HydraPubSubMessage message) {
        boolean published = false;

        while (!published) {
            try {
                final Runnable callback = message.getCallback();

                this.jedis.publish(message.getChannel(), message.getMessage());

                if (callback != null) {
                    callback.run();
                }

                published = true;
            } catch (Exception e) {
                this.checkRedis();
            }
        }
    }

    private void checkRedis() {
        try {
            this.jedis = this.hydraAPI.getRedisResource();
        } catch (Exception e) {
            HydraAPI.log(Level.SEVERE, String.format("[%s] Couldn't contact Redis server! Error: %s. Rechecking in 5 seconds.", this.getClass().getSimpleName(), e.getMessage()));

            try {
                Thread.sleep(5000);

                HydraAPI.log(Level.INFO, String.format("[%s] Rechecking Redis connection...", this.getClass().getSimpleName()));

                this.checkRedis();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

}
