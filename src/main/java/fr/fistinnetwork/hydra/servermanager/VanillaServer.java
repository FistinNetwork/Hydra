package fr.fistinnetwork.hydra.servermanager;

import fr.fistinnetwork.hydra.Hydra;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.concurrent.TimeUnit;

public class VanillaServer extends Server {
    protected ScheduledTask healthyTask;

    public VanillaServer() {
        super("vanilla", null, null, 10, new ServerOptions());
    }

    @Override
    public Integer getCurrentPlayers() {
        return ProxyServer.getInstance().getServerInfo(this.toString()).getPlayers().size();
    }

    @Override
    public void start() {
        this.healthyTask = Hydra.getInstance().getProxy().getScheduler().schedule(Hydra.getInstance(), this::checkStatus, 2, 2, TimeUnit.MINUTES);
    }

    @Override
    public boolean stop() {
        Hydra.getInstance().getProxy().getScheduler().cancel(healthyTask);
        return super.stop();
    }
}
