package fr.fistin.hydra.configuration;

public class HydraConfiguration {

    private String redisIp = "127.0.0.1";
    private int redisPort = 6379;
    private String redisPassword = "";
    private boolean production;

    public HydraConfiguration() {}

    public HydraConfiguration(String redisIp, int redisPort, String redisPassword, boolean production) {
        this.redisIp = redisIp;
        this.redisPort = redisPort;
        this.redisPassword = redisPassword;
        this.production = production;
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

    public boolean isProduction() {
        return this.production;
    }

    public void setProduction(boolean production) {
        this.production = production;
    }

}
