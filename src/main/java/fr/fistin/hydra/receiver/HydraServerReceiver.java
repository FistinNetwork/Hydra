package fr.fistin.hydra.receiver;

import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.api.protocol.packet.HydraPacket;
import fr.fistin.hydra.api.protocol.packet.common.HeartbeatPacket;
import fr.fistin.hydra.api.protocol.packet.server.ServerUpdateInfoPacket;
import fr.fistin.hydra.api.protocol.receiver.HydraPacketReceiver;
import fr.fistin.hydra.api.server.ServerState;
import fr.fistin.hydra.server.HydraServer;

public class HydraServerReceiver implements HydraPacketReceiver {

    private final Hydra hydra;

    public HydraServerReceiver(Hydra hydra) {
        this.hydra = hydra;
    }

    @Override
    public void receive(HydraPacket packet) {
        if (packet instanceof HeartbeatPacket) this.heartbeat((HeartbeatPacket) packet);
        else if (packet instanceof ServerUpdateInfoPacket) this.updateServerInfo((ServerUpdateInfoPacket) packet);
    }

    private void heartbeat(HeartbeatPacket packet) {
        final String serverId = packet.getApplicationId();
        final HydraServer server = this.hydra.getServerManager().getServerByName(serverId);

        if (server != null) {
            System.out.println("'" + serverId + "' server is now ready !");

            server.setCurrentState(ServerState.READY);
        }
    }

    private void updateServerInfo(ServerUpdateInfoPacket packet) {
        final HydraServer server = this.hydra.getServerManager().getServerByName(packet.getServerId());

        if (server != null) {
            server.setCurrentPlayers(packet.getPlayers());
            server.setCurrentState(packet.getCurrentState());

            this.hydra.getServerManager().sendServerInfoToRedis(server);
        }
    }

}
