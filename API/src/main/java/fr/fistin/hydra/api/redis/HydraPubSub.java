package fr.fistin.hydra.api.redis;

import fr.fistin.hydra.api.HydraAPI;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 20/11/2021 at 09:48
 */
public class HydraPubSub extends JedisPubSub {

    /** PubSub state. If <code>true</code>, PubSub are running */
    private boolean running;

    /** The sender of all messages */
    private final Sender sender;
    /** The sender thread */
    private Thread senderThread;
    /** The subscriber thread */
    private Thread subscriberThread;

    /** Map of all receivers subscribed */
    private final Map<String, Set<IHydraReceiver>> receivers;

    /** {@link HydraAPI} instance */
    private final HydraAPI hydraAPI;

    /**
     * Constructor of {@link HydraPubSub}
     *
     * @param hydraAPI {@link HydraAPI} instance
     */
    public HydraPubSub(HydraAPI hydraAPI) {
        this.hydraAPI = hydraAPI;
        this.receivers = new HashMap<>();
        this.sender = new Sender();
    }

    /**
     * Start PubSub
     */
    public void start() {
        HydraAPI.log("Starting PubSub...");

        this.running = true;

        this.senderThread = new Thread(this.sender, "PubSub Sender");
        this.senderThread.start();

        this.subscriberThread = new Thread(() -> {
            while (this.running) {
                try (final Jedis jedis = this.hydraAPI.getRedisResource()) {
                    if (jedis != null) {
                        jedis.psubscribe(this, HydraAPI.HYDRA_NAME + "*");
                    }

                    HydraAPI.log(Level.SEVERE, "Redis is no longer responding to subscriber!");
                    this.stop();
                }
            }
        }, "PubSub Subscriber");
        this.subscriberThread.start();
    }

    /**
     * Stop PubSub
     */
    public void stop() {
        HydraAPI.log("Stopping PubSub...");

        this.running = false;
        this.sender.running = false;

        if (this.isSubscribed()) {
            this.unsubscribe();
        }

        this.subscriberThread.interrupt();
        this.senderThread.interrupt();
    }

    /**
     * Subscribe a receiver on a given channel
     *
     * @param channel Channel
     * @param receiver Receiver to subscribe
     */
    public void subscribe(String channel, IHydraReceiver receiver) {
        final Set<IHydraReceiver> receivers = this.receivers.get(channel) != null ? this.receivers.get(channel) : ConcurrentHashMap.newKeySet();

        receivers.add(receiver);

        this.receivers.put(channel, receivers);

        if (this.isSubscribed()) {
            this.unsubscribe();
        }
    }

    /**
     * Unsubscribe a receiver from a given channel
     *
     * @param channel Receiver's channel
     * @param receiver Receiver to unsubscribe
     */
    public void unsubscribe(String channel, IHydraReceiver receiver) {
        try {
            final Set<IHydraReceiver> receivers = this.receivers.get(channel);

            if (receivers != null && receivers.contains(receiver)) {
                receivers.remove(receiver);

                this.receivers.put(channel, receivers);

                if (this.isSubscribed()) {
                    this.unsubscribe();
                }
            }
        } catch (Exception ignored) {}
    }

    /**
     * Send a given message on a channel
     *
     * @param channel Channel
     * @param message Message to send
     * @param callback Callback to fire after sending message
     */
    public void send(String channel, String message, Runnable callback) {
        this.sender.messages.add(new HydraPubSubMessage(channel, message, callback));
    }

    /**
     * Send a given message on a channel
     *
     * @param channel Channel
     * @param message Message to send
     */
    public void send(String channel, String message) {
        this.send(channel, message, null);
    }

    /**
     * Called when a message is received on PubSub
     *
     * @param channel Channel where the message is received
     * @param message The received message
     */
    @Override
    public void onPMessage(String pattern, String channel, String message) {
        final Set<IHydraReceiver> receivers = this.receivers.get(channel);

        if (receivers != null) {
            receivers.forEach(receiver -> receiver.receive(channel, message));
        }
    }

    /**
     * This class represents the PubSub sender
     */
    private class Sender implements Runnable {

        /** Queue of all remaining messages to send */
        private final LinkedBlockingQueue<HydraPubSubMessage> messages = new LinkedBlockingQueue<>();

        /** Send state. If <code>true</code>, the sender is currently running */
        private boolean running = true;
        /** {@link Jedis} instance */
        private Jedis jedis;

        @Override
        public void run() {
            this.checkRedis();

            while (this.running) {
                try {
                    this.processMessage(this.messages.take());
                } catch (InterruptedException e) {
                    this.jedis.close();
                    HydraAPI.log(Level.INFO, String.format("[%s] Interrupted!", this.getClass().getSimpleName()));
                    e.printStackTrace();
                    return;
                }
            }
        }

        private void processMessage(HydraPubSubMessage message) {
            final Runnable callback = message.getCallback();

            boolean published = false;

            while (!published) {
                try {

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
                this.jedis = hydraAPI.getRedisResource();
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

    }

}
