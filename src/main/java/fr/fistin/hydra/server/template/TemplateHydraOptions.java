package fr.fistin.hydra.server.template;

import fr.fistin.hydra.server.ServerOptions;

public class TemplateHydraOptions {

    private ServerOptions serverOptions;
    private int checkAlive;

    public TemplateHydraOptions(){}

    public TemplateHydraOptions(ServerOptions serverOptions, int checkAlive) {
        this.serverOptions = serverOptions;
        this.checkAlive = checkAlive;
    }

    public ServerOptions getServerOptions() {
        return this.serverOptions;
    }

    public void setServerOptions(ServerOptions serverOptions) {
        this.serverOptions = serverOptions;
    }

    public int getCheckAlive() {
        return this.checkAlive;
    }

    public void setCheckAlive(int checkAlive) {
        this.checkAlive = checkAlive;
    }

}
