package fr.fistin.hydra.configuration;

public class HydraConfiguration {

    private String redisIp = "127.0.0.1";
    private int redisPort = 6379;
    private String redisPassword = "";
    private String proxyPluginsUrl = "";

    public HydraConfiguration() {}

    public HydraConfiguration(String redisIp, int redisPort, String redisPassword, String proxyPluginsUrl) {
        this.redisIp = redisIp;
        this.redisPort = redisPort;
        this.redisPassword = redisPassword;
        this.proxyPluginsUrl = proxyPluginsUrl;
    }

    public String getRedisIp() {
        return this.redisIp;
    }

    public void setRedisIp(String redisIp) {
        this.redisIp = redisIp;
    }

    public int getRedisPort() {
        return this.redisPort;
    }

    public void setRedisPort(int redisPort) {
        this.redisPort = redisPort;
    }

    public String getRedisPassword() {
        return this.redisPassword;
    }

    public void setRedisPassword(String redisPassword) {
        this.redisPassword = redisPassword;
    }

    public String getProxyPluginsUrl() {
        return this.proxyPluginsUrl;
    }

    public void setProxyPluginsUrl(String proxyPluginsUrl) {
        this.proxyPluginsUrl = proxyPluginsUrl;
    }
}
