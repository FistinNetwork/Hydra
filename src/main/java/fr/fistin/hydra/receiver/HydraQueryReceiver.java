package fr.fistin.hydra.receiver;

import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.proxy.HydraProxy;
import fr.fistin.hydra.proxy.ProxyOptions;
import fr.fistin.hydra.server.HydraServer;
import fr.fistin.hydra.server.template.HydraTemplate;
import fr.fistin.hydraconnector.protocol.packet.HydraPacket;
import fr.fistin.hydraconnector.protocol.packet.proxy.AskForListProxyPacket;
import fr.fistin.hydraconnector.protocol.packet.proxy.ListProxyPacket;
import fr.fistin.hydraconnector.protocol.packet.proxy.StartProxyPacket;
import fr.fistin.hydraconnector.protocol.packet.proxy.StopProxyPacket;
import fr.fistin.hydraconnector.protocol.packet.server.*;
import fr.fistin.hydraconnector.protocol.receiver.HydraPacketReceiver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HydraQueryReceiver implements HydraPacketReceiver {

    private final Hydra hydra;

    public HydraQueryReceiver(Hydra hydra) {
        this.hydra = hydra;
    }

    @Override
    public void receive(HydraPacket packet) {
        if (packet instanceof StartServerPacket) this.startServer((StartServerPacket) packet);
        else if (packet instanceof StopServerPacket) this.stopServer((StopServerPacket) packet);
        else if (packet instanceof EvacuateServerPacket) this.evacuateServer((EvacuateServerPacket) packet);
        else if (packet instanceof StartProxyPacket) this.startProxy();
        else if (packet instanceof StopProxyPacket) this.stopProxy((StopProxyPacket) packet);
        else if (packet instanceof AskForListServerPacket) this.listServer((AskForListServerPacket) packet);
        else if (packet instanceof AskForServerInfoPacket) this.serverInfo((AskForServerInfoPacket) packet);
        else if (packet instanceof AskForListProxyPacket) this.listProxy((AskForListProxyPacket) packet);
    }

    private void startServer(StartServerPacket packet) {
        final HydraTemplate template = this.hydra.getTemplateManager().getTemplateByName(packet.getTemplateName());

        this.hydra.getServerManager().startServer(template);
    }

    private void stopServer(StopServerPacket packet) {
        this.hydra.getServerManager().getServerByName(packet.getServerId()).stop();
    }

    private void evacuateServer(EvacuateServerPacket packet) {
        this.hydra.getServerManager().evacuateServer(packet.getServerId(), packet.getDestinationServerId());
    }

    private void startProxy() {
        final HydraProxy proxy = new HydraProxy(this.hydra, new ProxyOptions(this.hydra.getConfiguration().getProxyPluginsUrl()));

        proxy.start();
    }

    private void stopProxy(StopProxyPacket packet) {
        this.hydra.getProxyManager().getProxyByName(packet.getProxyId()).stop();
    }

    private void listServer(AskForListServerPacket packet) {
        final List<String> servers = new ArrayList<>();

        for (Map.Entry<String, HydraServer> entry : this.hydra.getServerManager().getServers().entrySet()) servers.add(entry.getKey());

        final ListServerPacket outPacket = new ListServerPacket(servers);

        this.hydra.getHydraConnector().getConnectionManager().sendPacket(packet.getReturnChannel(), outPacket);
    }

    private void serverInfo(AskForServerInfoPacket packet) {
        final HydraServer server = this.hydra.getServerManager().getServerByName(packet.getServerId());

        if (server != null) {
            final ServerInfoPacket outPacket = new ServerInfoPacket(server.toString(), server.getCurrentPlayers(), server.getSlots(), server.getCurrentState());

            this.hydra.getHydraConnector().getConnectionManager().sendPacket(packet.getReturnChannel(), outPacket);
        }
    }

    private void listProxy(AskForListProxyPacket packet) {
        final List<String> proxies = new ArrayList<>();

        for (Map.Entry<String, HydraProxy> entry : this.hydra.getProxyManager().getProxies().entrySet()) proxies.add(entry.getKey());

        final ListProxyPacket outPacket = new ListProxyPacket(proxies);

        this.hydra.getHydraConnector().getConnectionManager().sendPacket(packet.getReturnChannel(), outPacket);
    }

}
