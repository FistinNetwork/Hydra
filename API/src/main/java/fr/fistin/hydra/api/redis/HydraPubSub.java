package fr.fistin.hydra.api.redis;

import fr.fistin.hydra.api.HydraAPI;
import redis.clients.jedis.JedisPubSub;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 20/11/2021 at 09:48
 */
public class HydraPubSub extends JedisPubSub {

    /** PubSub state. If <code>true</code>, PubSub are running */
    private boolean running;

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
    }

    /**
     * Start PubSub
     */
    public void start() {
        HydraAPI.log("Starting PubSub...");

        this.running = true;

        this.subscriberThread = new Thread(() -> {
            while (this.running) {
                this.hydraAPI.getRedis().process(jedis -> {
                    jedis.psubscribe(this, HydraAPI.HYDRA_NAME + "*");

                    HydraAPI.log(Level.SEVERE, "Redis is no longer responding to subscriber!");

                    this.stop();
                });
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

        if (this.isSubscribed()) {
            this.unsubscribe();
        }

        this.subscriberThread.interrupt();
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
    }

    /**
     * Unsubscribe a receiver from a given channel
     *
     * @param channel Receiver's channel
     * @param receiver Receiver to unsubscribe
     */
    public void unsubscribe(String channel, IHydraReceiver receiver) {
        final Set<IHydraReceiver> receivers = this.receivers.get(channel);

        if (receivers != null) {
            receivers.remove(receiver);

            this.receivers.put(channel, receivers);
        }
    }

    /**
     * Send a given message on a channel
     *
     * @param channel Channel
     * @param message Message to send
     */
    public void send(String channel, String message) {
        this.hydraAPI.getRedis().process(jedis -> jedis.publish(channel, message));
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

}
