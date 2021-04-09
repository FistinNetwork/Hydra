package fr.fistinnetwork.hydra.server.models;

import fr.fistinnetwork.hydra.Hydra;
import fr.fistinnetwork.hydra.scheduler.HydraTask;
import fr.fistinnetwork.hydra.server.Server;
import fr.fistinnetwork.hydra.server.ServerOptions;

import java.util.concurrent.TimeUnit;

public class VanillaServer extends Server {

    private HydraTask healthyTask;

    public VanillaServer(Hydra hydra) {
        super(hydra,"vanilla", null, null, 10, new ServerOptions());
    }

    @Override
    public void start() {
        this.healthyTask = this.hydra.getScheduler().schedule(this::checkStatus, 2, 2, TimeUnit.MINUTES);
        super.start();
    }

    @Override
    public boolean stop() {
        this.hydra.getScheduler().cancel(healthyTask.getId());
        return super.stop();
    }
}
