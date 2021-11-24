package fr.fistin.hydra.api.redis;

import fr.fistin.hydra.api.HydraAPI;
import fr.fistin.hydra.api.redis.receiver.IHydraChannelReceiver;
import fr.fistin.hydra.api.redis.receiver.IHydraPatternReceiver;
import redis.clients.jedis.Jedis;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 20/11/2021 at 09:48
 */
public class HydraPubSub {

    /** PubSub thread */
    private Thread senderThread;
    private Thread subscriberThread;

    /** PubSub state. If <code>true</code>, PubSub are running */
    private boolean running;

    /** PubSub publisher used to send message */
    private final HydraPubSubPublisher publisher;

    /** PubSub subscriber to subscribe on a channel */
    private final HydraPubSubSubscriber subscriber;

    /** {@link HydraAPI} instance */
    private final HydraAPI hydraAPI;

    /**
     * Constructor of {@link HydraPubSub}
     *
     * @param hydraAPI {@link HydraAPI} instance
     */
    public HydraPubSub(HydraAPI hydraAPI) {
        this.hydraAPI = hydraAPI;
        this.publisher = new HydraPubSubPublisher(hydraAPI);
        this.subscriber = new HydraPubSubSubscriber();

        this.start();
    }

    /**
     * Start PubSub
     */
    private void start() {
        this.running = true;

        this.senderThread = new Thread(publisher, "PubSubPublisherThread");
        this.senderThread.start();

        this.subscriberThread = new Thread(() -> {
            while (this.running) {
                final Jedis jedis = this.hydraAPI.getRedisResource();
                final String[] channels = this.subscriber.getChannelsSubscribed().toArray(new String[0]);

                if (channels.length > 0) {
                    jedis.subscribe(this.subscriber, channels);
                }

                final String[] patterns = this.subscriber.getPatternsSubscribed().toArray(new String[0]);

                if (patterns.length > 0) {
                    jedis.psubscribe(this.subscriber, patterns);
                }

                jedis.close();
            }
        });
        this.subscriberThread.start();
    }

    /**
     * Stop PubSub
     */
    public void stop() {
        this.publisher.setRunning(this.running = false);

        if (this.subscriber.isSubscribed()) {
            this.subscriber.unsubscribe();
            this.subscriber.punsubscribe();
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
    public void subscribe(String channel, IHydraChannelReceiver receiver) {
        this.subscriber.registerReceiver(channel, receiver);

        if (this.subscriber.isSubscribed()) {
            this.subscriber.unsubscribe();
        }
    }

    /**
     * Subscribe a receiver on a given pattern
     *
     * @param pattern Pattern
     * @param receiver Receiver to subscribe
     */
    public void subscribe(String pattern, IHydraPatternReceiver receiver) {
        this.subscriber.registerReceiver(pattern, receiver);

        if (this.subscriber.isSubscribed()) {
            this.subscriber.punsubscribe();
        }
    }

    /**
     * Send a given message on a channel
     *
     * @param channel Channel
     * @param message Message to send
     * @param callback Callback to fire after sending message
     */
    public void send(String channel, String message, Runnable callback) {
        this.publisher.publish(new HydraPubSubMessage(channel, message, callback));
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


}
