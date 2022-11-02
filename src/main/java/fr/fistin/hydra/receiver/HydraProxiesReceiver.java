package fr.fistin.hydra.receiver;

import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.api.protocol.HydraChannel;
import fr.fistin.hydra.api.protocol.heartbeat.HydraHeartbeatPacket;
import fr.fistin.hydra.api.protocol.packet.HydraPacket;
import fr.fistin.hydra.api.protocol.packet.HydraPacketHeader;
import fr.fistin.hydra.api.protocol.packet.IHydraPacketReceiver;
import fr.fistin.hydra.api.protocol.response.HydraResponse;
import fr.fistin.hydra.api.protocol.response.HydraResponseType;
import fr.fistin.hydra.api.proxy.HydraProxy;
import fr.fistin.hydra.api.proxy.packet.HydraUpdateProxyPacket;
import fr.fistin.hydra.api.server.HydraServer;
import fr.fistin.hydra.api.server.packet.HydraUpdateServerPacket;
import fr.fistin.hydra.proxy.HydraProxyManager;
import fr.fistin.hydra.server.HydraServerManager;

/**
 * Created by AstFaster
 * on 02/11/2022 at 11:24
 */
public class HydraProxiesReceiver implements IHydraPacketReceiver {

    private final HydraProxyManager proxyManager;

    public HydraProxiesReceiver() {
        this.proxyManager = Hydra.get().getProxyManager();
    }

    @Override
    public HydraResponse receive(HydraChannel channel, HydraPacketHeader header, HydraPacket packet) {
        if (packet instanceof final HydraUpdateProxyPacket proxyPacket) {
            final HydraProxy proxy = proxyPacket.getProxy();

            if (!header.getSender().equals(proxy.getName())) {
                return HydraResponseType.NOT_OK.asResponse().withMessage("Only the concerned proxy can update its information!");
            }

            this.proxyManager.updateProxy(proxy);

            return HydraResponseType.OK.asResponse();
        } else if (packet instanceof HydraHeartbeatPacket) {
            final HydraProxy proxy = Hydra.get().getAPI().getProxiesService().getProxy(header.getSender());

            if (proxy != null) {
                if (proxy.heartbeat()) {
                    this.proxyManager.updateProxy(proxy);
                } else {
                    this.proxyManager.saveProxy(proxy);
                }
                return HydraResponseType.OK.asResponse();
            }
            return HydraResponseType.NOT_OK.asResponse();
        }
        return HydraResponseType.NONE.asResponse();
    }

}
