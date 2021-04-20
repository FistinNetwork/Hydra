package fr.fistin.hydra.configuration;

public class HydraConfiguration {

    private boolean logFile = true;
    private String redisIp = "127.0.0.1";
    private int redisPort = 6379;
    private String redisPassword = "pass";

    public HydraConfiguration() {}

    public HydraConfiguration(boolean logFile, String redisIp, int redisPort, String redisPassword) {
        this.logFile = logFile;
        this.redisIp = redisIp;
        this.redisPort = redisPort;
        this.redisPassword = redisPassword;
    }

    public boolean isLogFile() {
        return this.logFile;
    }

    public void setLogFile(boolean logFile) {
        this.logFile = logFile;
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
}
