package fr.fistin.hydra.receiver;

import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.server.HydraServer;
import fr.fistin.hydraconnector.common.ServerState;
import fr.fistin.hydraconnector.protocol.packet.HydraPacket;
import fr.fistin.hydraconnector.protocol.packet.common.HeartbeatPacket;
import fr.fistin.hydraconnector.protocol.packet.server.UpdateServerInfoPacket;
import fr.fistin.hydraconnector.protocol.receiver.HydraPacketReceiver;

public class HydraServerReceiver implements HydraPacketReceiver {

    private final Hydra hydra;

    public HydraServerReceiver(Hydra hydra) {
        this.hydra = hydra;
    }

    @Override
    public void receive(HydraPacket packet) {
        if (packet instanceof HeartbeatPacket) this.heartbeat((HeartbeatPacket) packet);
        else if (packet instanceof UpdateServerInfoPacket) this.updateServerInfo((UpdateServerInfoPacket) packet);
    }

    private void heartbeat(HeartbeatPacket packet) {
        final String serverId = packet.getApplicationId();
        final HydraServer server = this.hydra.getServerManager().getServerByName(serverId);

        if (server != null) {
            System.out.println("'" + serverId + "' server is now ready !");

            server.setCurrentState(ServerState.READY);
        }
    }

    private void updateServerInfo(UpdateServerInfoPacket packet) {
        final HydraServer server = this.hydra.getServerManager().getServerByName(packet.getServerId());

        if (server != null) {
            server.setCurrentPlayers(packet.getPlayers());
            server.setCurrentState(packet.getCurrentState());

            this.hydra.getServerManager().sendServerInfoToRedis(server);
        }
    }

}
