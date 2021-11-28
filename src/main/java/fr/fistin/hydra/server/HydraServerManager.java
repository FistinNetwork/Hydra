package fr.fistin.hydra.server;

import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.api.event.model.HydraProxyStartEvent;
import fr.fistin.hydra.api.event.model.HydraServerStartEvent;
import fr.fistin.hydra.api.event.model.HydraServerStopEvent;
import fr.fistin.hydra.api.protocol.HydraChannel;
import fr.fistin.hydra.api.protocol.packet.model.proxy.HydraStopProxyPacket;
import fr.fistin.hydra.api.protocol.packet.model.server.HydraStopServerPacket;
import fr.fistin.hydra.api.protocol.response.HydraResponseCallback;
import fr.fistin.hydra.api.protocol.response.HydraResponseType;
import fr.fistin.hydra.api.proxy.HydraProxy;
import fr.fistin.hydra.api.server.HydraServer;
import fr.fistin.hydra.docker.swarm.DockerSwarm;

import java.util.HashSet;
import java.util.Set;

public class HydraServerManager {

    private final Set<HydraServer> servers;

    private final DockerSwarm swarm;

    private final Hydra hydra;

    public HydraServerManager(Hydra hydra) {
        this.hydra = hydra;
        this.swarm = this.hydra.getDocker().getSwarm();
        this.servers = new HashSet<>();
    }

    public void startServer() {
        final HydraServer server = new HydraServer("test");
        final HydraServerService service = new HydraServerService(server);

        this.swarm.runService(service);

        this.servers.add(server);

        this.hydra.getAPI().getEventBus().publish(new HydraServerStartEvent(server.getName()));

        System.out.println("Starting '" + server + "' server...");
    }

    public boolean stopServer(String name) {
        final HydraServer server = this.getServerByName(name);

        if (server != null) {
            final HydraStopServerPacket packet = new HydraStopServerPacket(name);
            final HydraResponseCallback responseCallback = (response, message) -> {
                if (response != HydraResponseType.OK) {
                    System.err.println("'" + server.getName() + "' server don't want to stop! Response: " + response + ". Reason: " + message + ". Forcing it to stop!");
                }

                this.swarm.removeService(name);

                this.hydra.getAPI().getEventBus().publish(new HydraServerStopEvent(server.getName()));

                System.out.println("Stopping '" + server.getName() + "' server...");
            };

            this.hydra.getAPI().getConnection().sendPacket(HydraChannel.SERVERS, packet).withResponseCallback(responseCallback).exec();

            return true;
        } else {
            System.err.println("Couldn't stop a server with the name: " + name + "!");
        }
        return false;
    }

    public HydraServer getServerByName(String name) {
        for (HydraServer server : this.servers) {
            if (server.getName().equals(name)) {
                return server;
            }
        }
        return null;
    }

    public Set<HydraServer> getServers() {
        return this.servers;
    }

}