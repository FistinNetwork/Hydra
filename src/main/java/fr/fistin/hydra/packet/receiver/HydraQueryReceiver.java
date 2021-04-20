package fr.fistin.hydra.packet.receiver;

import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.packet.HydraPacket;
import fr.fistin.hydra.packet.model.StartServerPacket;
import fr.fistin.hydra.server.HydraServer;
import fr.fistin.hydra.server.model.LobbyServer;

public class HydraQueryReceiver implements PacketReceiver {

    private final Hydra hydra;

    public HydraQueryReceiver(Hydra hydra) {
        this.hydra = hydra;
    }

    @Override
    public void receive(HydraPacket packet) {
        if (packet instanceof StartServerPacket) this.handleStartServer((StartServerPacket) packet);
    }

    private void handleStartServer(StartServerPacket packet) {
        System.out.println(packet.getType());
        final HydraServer server = new LobbyServer(this.hydra);
        this.hydra.getServerManager().startServer(server);
    }

}
