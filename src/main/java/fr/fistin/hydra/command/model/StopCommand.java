package fr.fistin.hydra.command.model;

import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.command.HydraCommand;

public class StopCommand extends HydraCommand {

    private final Hydra hydra;

    public StopCommand(Hydra hydra, String commandLabel) {
        super(commandLabel);
        this.hydra = hydra;
    }

    @Override
    public boolean execute(String[] args) {
        this.hydra.shutdown();
        return true;
    }

    @Override
    public String getHelp() {
        return "Stop Hydra application";
    }
}
