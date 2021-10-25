package fr.fistin.hydra.receiver;

import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.api.protocol.packet.HydraPacket;
import fr.fistin.hydra.api.protocol.packet.common.HeartbeatPacket;
import fr.fistin.hydra.api.protocol.receiver.HydraPacketReceiver;
import fr.fistin.hydra.proxy.HydraProxy;
import fr.fistin.hydra.proxy.ProxyState;

public class HydraProxyReceiver implements HydraPacketReceiver {

    private final Hydra hydra;

    public HydraProxyReceiver(Hydra hydra) {
        this.hydra = hydra;
    }

    @Override
    public void receive(HydraPacket packet) {
        if (packet instanceof HeartbeatPacket) this.heartbeat((HeartbeatPacket) packet);
    }

    private void heartbeat(HeartbeatPacket packet) {
        final String proxyId = packet.getApplicationId();
        final HydraProxy proxy = this.hydra.getProxyManager().getProxyByName(proxyId);

        if (proxy != null) {
            System.out.println("'" + proxyId + "' proxy is now ready !");

            proxy.setCurrentState(ProxyState.READY);
        }
    }

}
