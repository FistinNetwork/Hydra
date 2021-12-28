package fr.fistin.hydra.receiver;

import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.api.protocol.packet.HydraPacket;
import fr.fistin.hydra.api.protocol.packet.model.proxy.HydraStartProxyPacket;
import fr.fistin.hydra.api.protocol.packet.model.proxy.HydraStopProxyPacket;
import fr.fistin.hydra.api.protocol.packet.model.server.HydraStartServerPacket;
import fr.fistin.hydra.api.protocol.packet.model.server.HydraStopServerPacket;
import fr.fistin.hydra.api.protocol.packet.IHydraPacketReceiver;
import fr.fistin.hydra.api.protocol.response.HydraResponse;
import fr.fistin.hydra.api.protocol.response.HydraResponseType;
import fr.fistin.hydra.proxy.HydraProxyManager;
import fr.fistin.hydra.server.HydraServerManager;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 27/11/2021 at 10:44
 */
public class HydraQueryReceiver implements IHydraPacketReceiver {

    private final Hydra hydra;

    public HydraQueryReceiver(Hydra hydra) {
        this.hydra = hydra;
    }

    @Override
    public HydraResponse receive(String channel, HydraPacket packet) {
        final HydraServerManager serverManager = this.hydra.getServerManager();
        final HydraProxyManager proxyManager = this.hydra.getProxyManager();
        final HydraResponse response = new HydraResponse(HydraResponseType.NONE);

        if (packet instanceof HydraStartServerPacket) {
            serverManager.startServer(((HydraStartServerPacket) packet).getServerType());

            response.withType(HydraResponseType.OK).withMessage("Creating it...");
        } else if (packet instanceof HydraStopServerPacket) {
            if (serverManager.stopServer(((HydraStopServerPacket) packet).getServerName())) {
                response.withType(HydraResponseType.OK).withMessage("Stopping it...");
            } else {
                response.withType(HydraResponseType.NOT_OK).withMessage("Invalid server name!");
            }
        } else if (packet instanceof HydraStartProxyPacket) {
            proxyManager.startProxy();

            response.withType(HydraResponseType.OK).withMessage("Creating it...");
        } else if (packet instanceof HydraStopProxyPacket) {
            if (proxyManager.stopProxy(((HydraStopProxyPacket) packet).getProxyName())) {
                response.withType(HydraResponseType.OK).withMessage("Stopping it...");
            } else {
                response.withType(HydraResponseType.NOT_OK).withMessage("Invalid proxy name!");
            }
        }
        return response;
    }

}
