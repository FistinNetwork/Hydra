package fr.fistin.hydra.proxy;

import java.util.ArrayList;
import java.util.List;

public class ProxyOptions {

    private String pluginUrl;

    public ProxyOptions(String pluginUrl) {
        this.pluginUrl = pluginUrl;
    }

    public String getPluginUrl() {
        return this.pluginUrl;
    }

    public void setPluginUrl(String pluginUrl) {
        this.pluginUrl = pluginUrl;
    }

    public List<String> getEnvs() {
        final List<String> env = new ArrayList<>();

        env.add("TYPE=WATERFALL");
        env.add("PLUGINS=" + this.pluginUrl);

        return env;
    }

}
