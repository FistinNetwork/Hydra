package fr.fistin.hydra.query;

import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.server.template.HydraTemplate;
import fr.fistin.hydraconnector.protocol.packet.HydraPacket;
import fr.fistin.hydraconnector.protocol.packet.logger.LogPacket;
import fr.fistin.hydraconnector.protocol.packet.server.StartServerPacket;
import fr.fistin.hydraconnector.protocol.packet.server.StopServerPacket;
import fr.fistin.hydraconnector.protocol.receiver.HydraPacketReceiver;

import java.util.logging.Level;

public class HydraQueryReceiver implements HydraPacketReceiver {

    private final Hydra hydra;

    public HydraQueryReceiver(Hydra hydra) {
        this.hydra = hydra;
    }

    @Override
    public void receive(HydraPacket packet) {
        if (packet instanceof StartServerPacket) this.handleStartServer((StartServerPacket) packet);
        else if (packet instanceof StopServerPacket) this.handleStopServer((StopServerPacket) packet);
        else if (packet instanceof LogPacket) this.handleLog((LogPacket) packet);
    }

    private void handleStartServer(StartServerPacket packet) {
        final HydraTemplate template = this.hydra.getTemplateManager().getTemplateByName(packet.getTemplateName());
        this.hydra.getServerManager().startServer(template);
    }

    private void handleStopServer(StopServerPacket packet) {
        this.hydra.getServerManager().getServerByName(packet.getServerId()).stop();
    }

    private void handleLog(LogPacket packet) {
        if (packet.getType().equalsIgnoreCase("error")) {
            this.hydra.getLogger().log(Level.SEVERE, packet.getMessage());
        } else if (packet.getType().equalsIgnoreCase("warn")) {
            this.hydra.getLogger().log(Level.WARNING, packet.getMessage());
        } else {
            this.hydra.getLogger().log(Level.INFO, packet.getMessage());
        }
    }
}
