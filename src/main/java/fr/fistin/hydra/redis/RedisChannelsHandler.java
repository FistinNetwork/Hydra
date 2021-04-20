package fr.fistin.hydra.redis;

import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.packet.*;
import fr.fistin.hydra.packet.codec.IPacketDecoder;
import fr.fistin.hydra.packet.codec.IPacketEncoder;
import fr.fistin.hydra.packet.codec.PacketDecoder;
import fr.fistin.hydra.packet.codec.PacketEncoder;
import fr.fistin.hydra.packet.receiver.PacketReceiver;
import fr.fistin.hydra.util.logger.LogType;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.*;

public class RedisChannelsHandler extends JedisPubSub {

    private boolean loop;

    private final Map<String, Set<PacketReceiver>> packetReceivers;
    private final IPacketDecoder packetDecoder;
    private final IPacketEncoder packetEncoder;

    private final Hydra hydra;

    public RedisChannelsHandler(Hydra hydra) {
        this.hydra = hydra;
        this.packetReceivers = new HashMap<>();
        this.packetDecoder = new PacketDecoder(this.hydra);
        this.packetEncoder = new PacketEncoder();
        this.loop = true;
    }

    public void subscribe() {
        this.hydra.getScheduler().runTaskAsynchronously(() -> {
            while (this.loop) {
                final Jedis jedis = this.hydra.getRedisConnector().getResource();

                jedis.psubscribe(this, "*");

                this.hydra.getLogger().log(LogType.WARN, "Disconnected from database !");
                jedis.close();
            }
        });

        this.hydra.getLogger().log(LogType.INFO, "Subscribing PubSub...");

        while (!this.isSubscribed()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.hydra.getLogger().log(LogType.INFO, "Subscribed to PubSub.");
    }

    public void stop() {
        this.hydra.getLogger().log(LogType.INFO, "Stopping PubSub...");

        this.loop = false;
        this.unsubscribe();
        this.punsubscribe();
    }

    public void registerPacketReceiver(String channel, PacketReceiver packetReceiver) {
        Set<PacketReceiver> receivers = this.packetReceivers.get(channel);

        if (receivers == null) receivers = new HashSet<>();

        receivers.add(packetReceiver);
        this.packetReceivers.put(channel, receivers);
        this.subscribe(channel);

        this.hydra.getLogger().log(LogType.INFO, String.format("Registered a packet receiver: %s on channel: %s", packetReceiver.getClass().getSimpleName(), channel));
    }

    public void sendPacket(String channel, HydraPacket packet) {
        final Jedis jedis = this.hydra.getRedisConnector().getResource();

        jedis.publish(channel, this.packetEncoder.encode(packet));
        jedis.close();
    }

    @Override
    public void onMessage(String channel, String message) {
        final HydraPacket packet = this.packetDecoder.decode(message);

        if (packet != null) {
            final Set<PacketReceiver> receivers = this.packetReceivers.get(channel);

            if (receivers != null) receivers.forEach(packetReceiver -> packetReceiver.receive(packet));
        } else {
            this.hydra.getLogger().log(LogType.WARN, String.format("Receive an unknown packet from: %s channel. Message received: %s", channel, message));
        }
    }

}
