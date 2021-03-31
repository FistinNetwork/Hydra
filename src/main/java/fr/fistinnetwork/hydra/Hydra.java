package fr.fistinnetwork.hydra;

import fr.fistinnetwork.hydra.commands.BaseCommand;
import fr.fistinnetwork.hydra.docker.DockerAPI;
import fr.fistinnetwork.hydra.docker.DockerConnector;
import fr.fistinnetwork.hydra.servermanager.Server;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.Map;

public final class Hydra extends Plugin {

    private static Hydra instance;
    private DockerAPI dockerAPI;
    private DockerConnector docker;

    @Override
    public void onEnable() {
        instance = this;
        this.getLogger().info("Plugin fait par Faustin pour Fistin");
        this.getLogger().info("Lancement du système d'auto gestion");
        this.getProxy().getPluginManager().registerCommand(this, new BaseCommand());
        this.dockerAPI = new DockerAPI();
        this.docker = new DockerConnector();
    }

    @Override
    public void onDisable() {
        for(Map.Entry<String, Server> server : Server.getServerList().entrySet()){
            server.getValue().stop();
        }
        this.dockerAPI.unload();
    }

    public static Hydra getInstance(){
        return instance;
    }
    public DockerConnector getDocker(){
        return this.docker;
    }
    public DockerAPI getDockerAPI()
    {
        return this.dockerAPI;
    }
}