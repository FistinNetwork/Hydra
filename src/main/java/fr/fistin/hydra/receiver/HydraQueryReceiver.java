package fr.fistin.hydra.receiver;

import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.api.protocol.HydraChannel;
import fr.fistin.hydra.api.protocol.packet.HydraPacket;
import fr.fistin.hydra.api.protocol.packet.HydraPacketHeader;
import fr.fistin.hydra.api.protocol.packet.IHydraPacketReceiver;
import fr.fistin.hydra.api.protocol.response.HydraResponse;
import fr.fistin.hydra.api.protocol.response.HydraResponseType;
import fr.fistin.hydra.api.proxy.packet.HydraStartProxyPacket;
import fr.fistin.hydra.api.proxy.packet.HydraStopProxyPacket;
import fr.fistin.hydra.api.server.packet.HydraStartServerPacket;
import fr.fistin.hydra.api.server.packet.HydraStopServerPacket;
import fr.fistin.hydra.proxy.HydraProxyManager;
import fr.fistin.hydra.server.HydraServerManager;

/**
 * Created by AstFaster
 * on 02/11/2022 at 09:27
 */
public class HydraQueryReceiver implements IHydraPacketReceiver {

    private final HydraServerManager serverManager;
    private final HydraProxyManager proxyManager;

    public HydraQueryReceiver() {
        this.serverManager = Hydra.get().getServerManager();
        this.proxyManager = Hydra.get().getProxyManager();
    }

    @Override
    public HydraResponse receive(HydraChannel channel, HydraPacketHeader header, HydraPacket packet) {
        if (packet instanceof final HydraStartServerPacket serverPacket) {
            return new HydraResponse(HydraResponseType.OK).withMessage(this.serverManager.startServer(serverPacket.getServerInfo()));
        } else if (packet instanceof final HydraStopServerPacket serverPacket) {
            return (this.serverManager.stopServer(serverPacket.getServerName()) ? HydraResponseType.OK : HydraResponseType.NOT_OK).asResponse();
        } else if (packet instanceof final HydraStartProxyPacket proxyPacket) {
            return new HydraResponse(HydraResponseType.OK).withMessage(this.proxyManager.startProxy(proxyPacket.getProxyInfo()));
        } else if (packet instanceof final HydraStopProxyPacket proxyPacket) {
            return (this.proxyManager.stopProxy(proxyPacket.getProxyName()) ? HydraResponseType.OK : HydraResponseType.NOT_OK).asResponse();
        }
        return HydraResponseType.NONE.asResponse();
    }

}
