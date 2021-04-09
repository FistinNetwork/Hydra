package fr.fistin.hydra.configuration;

public class HydraConfiguration {

    //TODO Make a simple pojo with configuration keys
    private boolean logFile = true;

    public HydraConfiguration() {}

    public HydraConfiguration(boolean logFile) {
        this.logFile = logFile;
    }

    public boolean isLogFile() {
        return this.logFile;
    }

    public void setLogFile(boolean logFile) {
        this.logFile = logFile;
    }
}
