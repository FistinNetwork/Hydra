package fr.fistin.hydra.receiver;

import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.api.protocol.packet.HydraPacket;
import fr.fistin.hydra.api.protocol.packet.proxy.ProxyAskListPacket;
import fr.fistin.hydra.api.protocol.packet.proxy.ProxyListPacket;
import fr.fistin.hydra.api.protocol.packet.proxy.ProxyStartPacket;
import fr.fistin.hydra.api.protocol.packet.proxy.ProxyStopPacket;
import fr.fistin.hydra.api.protocol.packet.server.*;
import fr.fistin.hydra.api.protocol.receiver.HydraPacketReceiver;
import fr.fistin.hydra.proxy.HydraProxy;
import fr.fistin.hydra.proxy.ProxyOptions;
import fr.fistin.hydra.server.HydraServer;
import fr.fistin.hydra.server.template.HydraTemplate;

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
        if (packet instanceof ServerStartPacket) this.startServer((ServerStartPacket) packet);
        else if (packet instanceof ServerStopPacket) this.stopServer((ServerStopPacket) packet);
        else if (packet instanceof ServerEvacuatePacket) this.evacuateServer((ServerEvacuatePacket) packet);
        else if (packet instanceof ProxyStartPacket) this.startProxy();
        else if (packet instanceof ProxyStopPacket) this.stopProxy((ProxyStopPacket) packet);
        else if (packet instanceof ServerAskListPacket) this.listServer((ServerAskListPacket) packet);
        else if (packet instanceof ServerAskInfoPacket) this.serverInfo((ServerAskInfoPacket) packet);
        else if (packet instanceof ProxyAskListPacket) this.listProxy((ProxyAskListPacket) packet);
    }

    private void startServer(ServerStartPacket packet) {
        final HydraTemplate template = this.hydra.getTemplateManager().getTemplateByName(packet.getTemplateName());

        this.hydra.getServerManager().startServer(template);
    }

    private void stopServer(ServerStopPacket packet) {
        this.hydra.getServerManager().getServerByName(packet.getServerId()).stop();
    }

    private void evacuateServer(ServerEvacuatePacket packet) {
        this.hydra.getServerManager().evacuateServer(packet.getServerId(), packet.getDestinationServerId());
    }

    private void startProxy() {
        final HydraProxy proxy = new HydraProxy(this.hydra, new ProxyOptions(this.hydra.getConfiguration().getProxyPluginsUrl()));

        proxy.start();
    }

    private void stopProxy(ProxyStopPacket packet) {
        this.hydra.getProxyManager().getProxyByName(packet.getProxyId()).stop();
    }

    private void listServer(ServerAskListPacket packet) {
        final List<String> servers = new ArrayList<>();

        for (Map.Entry<String, HydraServer> entry : this.hydra.getServerManager().getServers().entrySet()) servers.add(entry.getKey());

        final ServerListPacket outPacket = new ServerListPacket(servers);

        this.hydra.getAPI().getConnectionManager().sendPacket(packet.getReturnChannel(), outPacket);
    }

    private void serverInfo(ServerAskInfoPacket packet) {
        final HydraServer server = this.hydra.getServerManager().getServerByName(packet.getServerId());

        if (server != null) {
            final ServerInfoPacket outPacket = new ServerInfoPacket(server.toString(), server.getCurrentPlayers(), server.getCurrentState());

            this.hydra.getAPI().getConnectionManager().sendPacket(packet.getReturnChannel(), outPacket);
        }
    }

    private void listProxy(ProxyAskListPacket packet) {
        final List<String> proxies = new ArrayList<>();

        for (Map.Entry<String, HydraProxy> entry : this.hydra.getProxyManager().getProxies().entrySet()) proxies.add(entry.getKey());

        final ProxyListPacket outPacket = new ProxyListPacket(proxies);

        this.hydra.getAPI().getConnectionManager().sendPacket(packet.getReturnChannel(), outPacket);
    }

}
