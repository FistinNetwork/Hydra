package fr.fistin.hydra.query;

import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.server.template.HydraTemplate;
import fr.fistin.hydraconnector.protocol.packet.HydraPacket;
import fr.fistin.hydraconnector.protocol.packet.server.StartServerPacket;
import fr.fistin.hydraconnector.protocol.packet.server.StopServerPacket;
import fr.fistin.hydraconnector.protocol.receiver.HydraPacketReceiver;

public class HydraQueryReceiver implements HydraPacketReceiver {

    private final Hydra hydra;

    public HydraQueryReceiver(Hydra hydra) {
        this.hydra = hydra;
    }

    @Override
    public void receive(HydraPacket packet) {
        if (packet instanceof StartServerPacket) this.handleStartServer((StartServerPacket) packet);
        else if (packet instanceof StopServerPacket) this.handleStopServer((StopServerPacket) packet);
    }

    private void handleStartServer(StartServerPacket packet) {
        final HydraTemplate template = this.hydra.getTemplateManager().getTemplateByName(packet.getTemplateName());
        this.hydra.getServerManager().startServer(template);
    }

    private void handleStopServer(StopServerPacket packet) {
        this.hydra.getServerManager().getServerByName(packet.getServerId()).stop();
    }
}
