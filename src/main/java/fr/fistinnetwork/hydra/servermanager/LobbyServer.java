package fr.fistinnetwork.hydra.servermanager;

import fr.fistinnetwork.hydra.Hydra;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.concurrent.TimeUnit;

public class LobbyServer extends Server {
    protected ScheduledTask healthyTask;

    public LobbyServer() {
        super("lobby", "https://fistincdn.blob.core.windows.net/serverdata/lobby/world.zip", "https://fistincdn.blob.core.windows.net/serverdata/lobby/plugins.zip", 10, new ServerOptions());
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
        Hydra.getInstance().getProxy().getScheduler().cancel(this.healthyTask);
        return super.stop();
    }
}
