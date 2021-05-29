package fr.fistin.hydra.receiver;

import fr.fistin.hydra.Hydra;
import fr.fistin.hydraconnector.protocol.packet.HydraPacket;
import fr.fistin.hydraconnector.protocol.packet.common.HeartbeatPacket;
import fr.fistin.hydraconnector.protocol.receiver.HydraPacketReceiver;

public class HydraServerReceiver implements HydraPacketReceiver {

    private final Hydra hydra;

    public HydraServerReceiver(Hydra hydra) {
        this.hydra = hydra;
    }

    @Override
    public void receive(HydraPacket packet) {
        if (packet instanceof HeartbeatPacket) this.heartbeat((HeartbeatPacket) packet);
    }

    private void heartbeat(HeartbeatPacket packet) {
        final String serverId = packet.getApplicationId();

        System.out.println("'" + serverId + "' server is now ready !");

        this.hydra.getServerManager().getServerByName(serverId).checkStatus();
    }

}
