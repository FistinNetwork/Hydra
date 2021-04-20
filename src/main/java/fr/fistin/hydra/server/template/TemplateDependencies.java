package fr.fistin.hydra.server.template;

public class TemplateDependencies {

    private String mapUrl;
    private String pluginUrl;

    public TemplateDependencies() {}

    public TemplateDependencies(String mapUrl, String pluginUrl) {
        this.mapUrl = mapUrl;
        this.pluginUrl = pluginUrl;
    }

    public String getMapUrl() {
        return this.mapUrl;
    }

    public void setMapUrl(String mapUrl) {
        this.mapUrl = mapUrl;
    }

    public String getPluginUrl() {
        return this.pluginUrl;
    }

    public void setPluginUrl(String pluginUrl) {
        this.pluginUrl = pluginUrl;
    }
}
