package fr.fistin.hydra.receiver;

import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.proxy.HydraProxy;
import fr.fistin.hydra.proxy.ProxyState;
import fr.fistin.hydra.server.HydraServer;
import fr.fistin.hydraconnector.protocol.channel.HydraChannel;
import fr.fistin.hydraconnector.protocol.packet.HydraPacket;
import fr.fistin.hydraconnector.protocol.packet.common.HeartbeatPacket;
import fr.fistin.hydraconnector.protocol.packet.proxy.AskForListProxyPacket;
import fr.fistin.hydraconnector.protocol.packet.proxy.ListProxyPacket;
import fr.fistin.hydraconnector.protocol.packet.server.ListServerPacket;
import fr.fistin.hydraconnector.protocol.receiver.HydraPacketReceiver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HydraProxyReceiver implements HydraPacketReceiver {

    private final Hydra hydra;

    public HydraProxyReceiver(Hydra hydra) {
        this.hydra = hydra;
    }

    @Override
    public void receive(HydraPacket packet) {
        if (packet instanceof HeartbeatPacket) this.heartbeat((HeartbeatPacket) packet);
        else if (packet instanceof AskForListProxyPacket) this.listProxy();
    }

    private void heartbeat(HeartbeatPacket packet) {
        final String proxyId = packet.getApplicationId();
        final HydraProxy proxy = this.hydra.getProxyManager().getProxyByName(proxyId);

        if (proxy != null) {
            System.out.println("'" + proxyId + "' proxy is now ready !");

            proxy.setCurrentState(ProxyState.READY);
        }
    }

    private void listProxy() {
        final List<String> proxies = new ArrayList<>();

        for (Map.Entry<String, HydraProxy> entry : this.hydra.getProxyManager().getProxies().entrySet()) proxies.add(entry.getKey());

        final ListProxyPacket packet = new ListProxyPacket(proxies);

        this.hydra.getHydraConnector().getConnectionManager().sendPacket(HydraChannel.PROXIES, packet);
        this.hydra.getHydraConnector().getConnectionManager().sendPacket(HydraChannel.SERVERS, packet);
    }

}
