package fr.fistin.hydraconnector.redis;

import fr.fistin.hydraconnector.HydraConnector;
import fr.fistin.hydraconnector.protocol.channel.HydraChannel;
import fr.fistin.hydraconnector.protocol.codec.IPacketDecoder;
import fr.fistin.hydraconnector.protocol.codec.PacketDecoder;
import fr.fistin.hydraconnector.protocol.packet.HydraPacket;
import fr.fistin.hydraconnector.protocol.receiver.HydraPacketReceiver;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RedisHandler extends JedisPubSub {

    private boolean loop;

    private final Map<String, Set<HydraPacketReceiver>> packetReceivers;
    private final IPacketDecoder packetDecoder;

    private final HydraConnector hydraConnector;

    public RedisHandler(HydraConnector hydraConnector) {
        this.hydraConnector = hydraConnector;
        this.packetReceivers = new HashMap<>();
        this.packetDecoder = new PacketDecoder(this.hydraConnector);
        this.loop = true;
    }

    public void subscribe() {
        new Thread(() -> {
            while (this.loop) {
                final Jedis jedis = this.hydraConnector.getRedisConnection().getJedis();

                jedis.psubscribe(this, "*");

                this.hydraConnector.log(String.format("[%s] Disconnected from database !", "WARN"));
                jedis.close();
            }
        }).start();

        this.hydraConnector.log("Subscribing PubSub...");

        while (!this.isSubscribed()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.hydraConnector.log("Subscribed to PubSub.");
    }

    public void stop() {
        this.hydraConnector.log("Stopping PubSub...");

        this.loop = false;
        this.unsubscribe();
        this.punsubscribe();
    }

    public void registerPacketReceiver(HydraChannel channel, HydraPacketReceiver packetReceiver) {
        this.registerPacketReceiver(channel.getName(), packetReceiver);
    }

    public void registerPacketReceiver(String channel, HydraPacketReceiver packetReceiver) {
        Set<HydraPacketReceiver> receivers = this.packetReceivers.get(channel);

        if (receivers == null) receivers = new HashSet<>();

        receivers.add(packetReceiver);
        this.packetReceivers.put(channel, receivers);
        this.subscribe(channel);

        this.hydraConnector.log(String.format("Registered a packet receiver: %s on channel: %s", packetReceiver.getClass().getSimpleName(), channel));
    }

    @Override
    public void onMessage(String channel, String message) {
        final HydraPacket packet = this.packetDecoder.decode(message);

        if (packet != null) {
            final Set<HydraPacketReceiver> receivers = this.packetReceivers.get(channel);

            if (receivers != null) receivers.forEach(packetReceiver -> packetReceiver.receive(packet));
        } else {
            this.hydraConnector.log(String.format("[%s] Receive an unknown packet from: %s channel. Message received: %s", "WARN", channel, message));
        }
    }
}
