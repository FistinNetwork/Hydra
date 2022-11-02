package fr.fistin.hydra.receiver;

import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.api.HydraAPI;
import fr.fistin.hydra.api.protocol.HydraChannel;
import fr.fistin.hydra.api.protocol.heartbeat.HydraHeartbeatPacket;
import fr.fistin.hydra.api.protocol.packet.HydraPacket;
import fr.fistin.hydra.api.protocol.packet.HydraPacketHeader;
import fr.fistin.hydra.api.protocol.packet.IHydraPacketReceiver;
import fr.fistin.hydra.api.protocol.response.HydraResponse;
import fr.fistin.hydra.api.protocol.response.HydraResponseType;
import fr.fistin.hydra.api.server.HydraServer;
import fr.fistin.hydra.api.server.packet.HydraUpdateServerPacket;
import fr.fistin.hydra.proxy.HydraProxyManager;
import fr.fistin.hydra.server.HydraServerManager;

/**
 * Created by AstFaster
 * on 02/11/2022 at 11:24
 */
public class HydraServersReceiver implements IHydraPacketReceiver {

    private final HydraServerManager serverManager;

    public HydraServersReceiver() {
        this.serverManager = Hydra.get().getServerManager();
    }

    @Override
    public HydraResponse receive(HydraChannel channel, HydraPacketHeader header, HydraPacket packet) {
        if (packet instanceof final HydraUpdateServerPacket serverPacket) {
            final HydraServer server = serverPacket.getServer();

            if (!header.getSender().equals(server.getName())) {
                return HydraResponseType.NOT_OK.asResponse().withMessage("Only the concerned server can update its information!");
            }

            this.serverManager.updateServer(server);

            return HydraResponseType.OK.asResponse();
        } else if (packet instanceof HydraHeartbeatPacket) {
            final HydraServer server = Hydra.get().getAPI().getServersService().getServer(header.getSender());

            if (server != null) {
                if (server.heartbeat()) {
                    this.serverManager.updateServer(server);
                } else {
                    this.serverManager.saveServer(server);
                }
                return HydraResponseType.OK.asResponse();
            }
            return HydraResponseType.NOT_OK.asResponse();
        }
        return HydraResponseType.NONE.asResponse();
    }

}
