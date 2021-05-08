package fr.fistin.hydra.command.model;

import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.command.HydraCommand;

public class HelpCommand extends HydraCommand {

    private final Hydra hydra;

    public HelpCommand(Hydra hydra, String commandLabel) {
        super(commandLabel);
        this.hydra = hydra;
    }

    @Override
    public boolean execute(String[] args) {
        this.hydra.getCommandManager().showHelp();
        return true;
    }

    @Override
    public String getHelp() {
        return "Show the help";
    }
}
