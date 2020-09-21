package fr.fistin.hydra;

import fr.fistin.hydra.commands.baseCommand;
import fr.fistin.hydra.docker.DockerConnector;
import fr.fistin.hydra.servermanager.Server;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.Map;

public final class Hydra extends Plugin {

    private static Hydra instance;
    private DockerConnector docker;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("Plugin fait par Faustin pour Fistin");
        getLogger().info("Lancement du système d'auto gestion");
        getProxy().getPluginManager().registerCommand(this, new baseCommand());
        this.docker = new DockerConnector();
    }

    @Override
    public void onDisable() {
        for(Map.Entry<String, Server> server: Server.getServerList().entrySet()){

        }
    }

    public static Hydra getInstance(){
        return instance;
    }
    public DockerConnector getDocker(){
        return docker;
    }
}