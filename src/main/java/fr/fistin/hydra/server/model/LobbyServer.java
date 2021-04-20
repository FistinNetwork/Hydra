package fr.fistin.hydra.server.model;

import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.scheduler.HydraTask;
import fr.fistin.hydra.server.HydraServer;
import fr.fistin.hydra.server.ServerOptions;
import fr.fistin.hydra.server.ServerState;

import java.util.concurrent.TimeUnit;

public class LobbyServer extends HydraServer {

    private HydraTask healthyTask;

    public LobbyServer(Hydra hydra) {
        super(hydra,"lobby", "https://fistincdn.blob.core.windows.net/serverdata/lobby/world.zip", "https://fistincdn.blob.core.windows.net/serverdata/lobby/plugins.zip", 10, new ServerOptions());
    }

    @Override
    public void start() {
        this.healthyTask = this.hydra.getScheduler().schedule(() -> {
            if (this.currentState != ServerState.SHUTDOWN) this.checkStatus();
        }, 2, 2, TimeUnit.MINUTES);
        super.start();
    }

    @Override
    public boolean stop() {
        this.hydra.getScheduler().cancel(healthyTask.getId());
        return super.stop();
    }

}