package fr.fistin.hydra.command;

public abstract class HydraCommand {

    protected String commandLabel;

    public HydraCommand(String commandLabel) {
        this.commandLabel = commandLabel;
    }

    public abstract boolean execute(String[] args);

    public String getCommandLabel() {
        return this.commandLabel;
    }

    public abstract String getHelp();
}
