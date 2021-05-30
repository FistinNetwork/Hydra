package fr.fistin.hydra.receiver;

import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.server.HydraServer;
import fr.fistin.hydraconnector.api.ServerState;
import fr.fistin.hydraconnector.protocol.channel.HydraChannel;
import fr.fistin.hydraconnector.protocol.packet.HydraPacket;
import fr.fistin.hydraconnector.protocol.packet.common.HeartbeatPacket;
import fr.fistin.hydraconnector.protocol.packet.server.*;
import fr.fistin.hydraconnector.protocol.receiver.HydraPacketReceiver;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HydraServerReceiver implements HydraPacketReceiver {

    private final Hydra hydra;

    public HydraServerReceiver(Hydra hydra) {
        this.hydra = hydra;
    }

    @Override
    public void receive(HydraPacket packet) {
        if (packet instanceof HeartbeatPacket) this.heartbeat((HeartbeatPacket) packet);
        else if (packet instanceof AskForListServerPacket) this.listServer();
        else if (packet instanceof AskForServerInfoPacket) this.serverInfo((AskForServerInfoPacket) packet);
        else if (packet instanceof UpdateServerInfoPacket) this.updateServerInfo((UpdateServerInfoPacket) packet);
    }

    private void heartbeat(HeartbeatPacket packet) {
        final String serverId = packet.getApplicationId();

        System.out.println("'" + serverId + "' server is now ready !");

        this.hydra.getServerManager().getServerByName(serverId).setCurrentState(ServerState.READY);
    }

    private void listServer() {
        final List<String> servers = new ArrayList<>();

        for (Map.Entry<String, HydraServer> entry : this.hydra.getServerManager().getServers().entrySet()) servers.add(entry.getKey());

        final ListServerPacket packet = new ListServerPacket(servers);

        this.hydra.getHydraConnector().getConnectionManager().sendPacket(HydraChannel.SERVERS, packet);
    }

    private void serverInfo(AskForServerInfoPacket packet) {
        final HydraServer server = this.hydra.getServerManager().getServerByName(packet.getServerId());

        final ServerInfoPacket outPacket = new ServerInfoPacket(server.toString(), server.getCurrentPlayers(), server.getSlots(), server.getCurrentState());

        this.hydra.getHydraConnector().getConnectionManager().sendPacket(HydraChannel.SERVERS, outPacket);
    }

    private void updateServerInfo(UpdateServerInfoPacket packet) {
        final HydraServer server = this.hydra.getServerManager().getServerByName(packet.getServerId());

        server.setCurrentPlayers(packet.getPlayers());
        server.setCurrentState(packet.getCurrentState());

        this.hydra.getServerManager().sendServerInfoToRedis(server);
    }

}
