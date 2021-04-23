package fr.fistin.hydra.server.template;

import fr.fistin.hydra.server.ServerOptions;

public class TemplateHydraOptions {

    private int slots;
    private ServerOptions serverOptions;
    private int checkAlive;

    public TemplateHydraOptions(){}

    public TemplateHydraOptions(int slots, ServerOptions serverOptions, int checkAlive) {
        this.slots = slots;
        this.serverOptions = serverOptions;
        this.checkAlive = checkAlive;
    }

    public int getSlots() {
        return this.slots;
    }

    public void setSlots(int slots) {
        this.slots = slots;
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
