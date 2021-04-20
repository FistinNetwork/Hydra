package fr.fistin.hydra.server.template;

public class TemplateStartingOptions {

    private int timeToWait;
    private int playersOnNetwork;

    public TemplateStartingOptions(){}

    public TemplateStartingOptions(int timeToWait, int playersOnNetwork) {
        this.timeToWait = timeToWait;
        this.playersOnNetwork = playersOnNetwork;
    }

    public int getTimeToWait() {
        return this.timeToWait;
    }

    public void setTimeToWait(int timeToWait) {
        this.timeToWait = timeToWait;
    }

    public int getPlayersOnNetwork() {
        return this.playersOnNetwork;
    }

    public void setPlayersOnNetwork(int playersOnNetwork) {
        this.playersOnNetwork = playersOnNetwork;
    }
}
