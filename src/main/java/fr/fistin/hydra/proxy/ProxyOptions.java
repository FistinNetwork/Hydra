package fr.fistin.hydra.proxy;

import java.util.ArrayList;
import java.util.List;

public class ProxyOptions {

    private String[] plugins = {};

    public String[] getPlugins() {
        return this.plugins;
    }

    public void setPlugins(String[] plugins) {
        this.plugins = plugins;
    }

    public List<String> getEnvs() {
        final List<String> env = new ArrayList<>();
        final StringBuilder plugins = new StringBuilder();

        for (String plugin : this.plugins) {
            plugins.append(plugin).append(",");
        }

        env.add(plugins.toString());
        env.add("TYPE=WATERFALL");

        return env;
    }

}
