package fr.fistin.hydra.server.models;

import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.scheduler.HydraTask;
import fr.fistin.hydra.server.Server;
import fr.fistin.hydra.server.ServerOptions;
import fr.fistin.hydra.server.ServerState;

import java.util.concurrent.TimeUnit;

public class VanillaServer extends Server {

    private HydraTask healthyTask;

    public VanillaServer(Hydra hydra) {
        super(hydra,"vanilla", null, null, 10, new ServerOptions());
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
