package fr.fistin.hydra.server.template;

import fr.fistin.hydra.server.ServerOptions;

public class TemplateHydraOptions {

    private int slots;
    private ServerOptions serverOptions;
    private int timeout;

    public TemplateHydraOptions(){}

    public TemplateHydraOptions(int slots, ServerOptions serverOptions, int timeout) {
        this.slots = slots;
        this.serverOptions = serverOptions;
        this.timeout = timeout;
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

    public int getTimeout() {
        return this.timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
