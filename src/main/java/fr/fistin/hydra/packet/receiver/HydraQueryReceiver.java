package fr.fistin.hydra.packet.receiver;

import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.packet.HydraPacket;
import fr.fistin.hydra.packet.model.StartServerPacket;
import fr.fistin.hydra.server.template.HydraTemplate;

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
        final HydraTemplate template = this.hydra.getTemplateManager().getTemplateByName(packet.getName());
        this.hydra.getServerManager().startServer(template);
    }

}
