package fr.fistin.hydraconnector.protocol;

import fr.fistin.hydraconnector.HydraConnector;
import fr.fistin.hydraconnector.protocol.channel.HydraChannel;
import fr.fistin.hydraconnector.protocol.codec.IPacketEncoder;
import fr.fistin.hydraconnector.protocol.codec.PacketEncoder;
import fr.fistin.hydraconnector.protocol.packet.HydraPacket;
import redis.clients.jedis.Jedis;

public class HydraConnectionManager {

    private final IPacketEncoder packetEncoder;

    private final HydraConnector hydraConnector;

    public HydraConnectionManager(HydraConnector hydraConnector) {
        this.hydraConnector = hydraConnector;
        this.packetEncoder = new PacketEncoder(this.hydraConnector);
    }

    public void sendPacket(HydraPacket packet) {
        this.sendPacket(HydraChannel.DEFAULT, packet);
    }

    public void sendPacket(HydraChannel channel, HydraPacket packet) {
        this.sendPacket(channel.getName(), packet);
    }

    public void sendPacket(String channel, HydraPacket packet) {
        final Jedis jedis = this.hydraConnector.getRedisConnection().getJedis();

        jedis.publish(channel, this.packetEncoder.encode(packet));
        jedis.close();
    }

}
