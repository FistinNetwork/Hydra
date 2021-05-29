package fr.fistin.hydra.receiver;

import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.proxy.HydraProxy;
import fr.fistin.hydra.proxy.ProxyOptions;
import fr.fistin.hydra.server.template.HydraTemplate;
import fr.fistin.hydraconnector.protocol.packet.HydraPacket;
import fr.fistin.hydraconnector.protocol.packet.proxy.StartProxyPacket;
import fr.fistin.hydraconnector.protocol.packet.proxy.StopProxyPacket;
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
        if (packet instanceof StartServerPacket) this.startServer((StartServerPacket) packet);
        else if (packet instanceof StopServerPacket) this.stopServer((StopServerPacket) packet);
        else if (packet instanceof StartProxyPacket) this.startProxy();
        else if (packet instanceof StopProxyPacket) this.stopProxy((StopProxyPacket) packet);
    }

    private void startServer(StartServerPacket packet) {
        final HydraTemplate template = this.hydra.getTemplateManager().getTemplateByName(packet.getTemplateName());

        this.hydra.getServerManager().startServer(template);
    }

    private void stopServer(StopServerPacket packet) {
        this.hydra.getServerManager().getServerByName(packet.getServerId()).stop();
    }

    private void startProxy() {
        final HydraProxy proxy = new HydraProxy(this.hydra, new ProxyOptions(this.hydra.getConfiguration().getProxyPluginsUrl()));

        proxy.start();
    }

    private void stopProxy(StopProxyPacket packet) {
        this.hydra.getProxyManager().getProxyByName(packet.getProxyId()).stop();
    }

}
