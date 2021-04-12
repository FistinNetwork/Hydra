package fr.fistin.hydra.configuration;

public class HydraConfiguration {

    private boolean logFile = true;
    private String redisIp;
    private int redisPort;
    private String redisPassword;

    public HydraConfiguration() {}

    public HydraConfiguration(boolean logFile, String redisIp, int redisPort, String redisPassword) {
        this.logFile = logFile;
        this.redisIp = redisIp;
        this.redisPort = redisPort;
        this.redisPassword = redisPassword;
    }

    public HydraConfiguration(boolean logFile) {
        this.logFile = logFile;
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
