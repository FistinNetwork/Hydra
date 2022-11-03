package fr.fistin.hydra.server;

import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.api.HydraAPI;
import fr.fistin.hydra.api.server.HydraServer;
import fr.fistin.hydra.api.server.HydraServerCreationInfo;
import fr.fistin.hydra.api.server.HydraServersService;
import fr.fistin.hydra.api.server.event.HydraServerStartedEvent;
import fr.fistin.hydra.api.server.event.HydraServerStoppedEvent;
import fr.fistin.hydra.api.server.event.HydraServerUpdatedEvent;

public class HydraServerManager {

    private final HydraServersService serversService;
    private final HydraServersHandler handler;

    private final Hydra hydra;

    public HydraServerManager(Hydra hydra) {
        this.hydra = hydra;
        this.serversService = this.hydra.getAPI().getServersService();
        this.handler = new HydraServersHandler();
    }

    public HydraServer startServer(HydraServerCreationInfo serverInfo) {
        final HydraServer server = new HydraServer(serverInfo);

        this.saveServer(server);

        this.handler.startServer(server);
        this.hydra.getAPI().getEventBus().publish(new HydraServerStartedEvent(server));

        System.out.println("Started '" + server + "' server.");

        return server;
    }

    public boolean stopServer(String name) {
        final HydraServer server = this.serversService.getServer(name);

        this.handler.stopServer(name);

        if (server == null) {
            System.err.println("Couldn't stop a server with the name: " + name + "!");
            return false;
        }

        server.setState(HydraServer.State.SHUTDOWN);

        this.updateServer(server);

        this.hydra.getAPI().getEventBus().publish(new HydraServerStoppedEvent(server));
        this.hydra.getRedis().process(jedis -> jedis.del(HydraServersService.HASH + server.getType() + ":" + server.getName()));

        System.out.println("Stopped '" + name + "' server.");

        return true;
    }

    public void updateServer(HydraServer server) {
        this.saveServer(server);
        this.hydra.getAPI().getEventBus().publish(new HydraServerUpdatedEvent(server));
    }

    public void saveServer(HydraServer server) {
        this.hydra.getRedis().process(jedis -> jedis.set(HydraServersService.HASH + server.getType() + ":" + server.getName(), HydraAPI.GSON.toJson(server)));
    }

}