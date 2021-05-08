package fr.fistin.hydra.command;

import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.util.ChatColor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class HydraCommandManager {

    private final List<HydraCommand> commands;

    private final Hydra hydra;

    public HydraCommandManager(Hydra hydra) {
        this.hydra = hydra;
        this.commands = new ArrayList<>();
    }

    public void start() {
        while (this.hydra.isRunning()) {
            String line = null;

            try {
                line = this.hydra.getConsoleReader().readLine(">");
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (line != null) {
                this.inputCommand(line);
            }
        }
    }

    public void inputCommand(String data) {
        String[] args = data.split(" ");
        final String commandLabel = args[0];

        args = Arrays.copyOfRange(args, 1, args.length);

        for (HydraCommand command : this.commands) {
            if (command.getCommandLabel().equalsIgnoreCase(commandLabel)) {
                if (!command.execute(args)) {
                    this.hydra.getLogger().log(Level.SEVERE, String.format("An error occurred while executing %s command !", command.getCommandLabel()));
                }
                return;
            }
        }

        this.hydra.getLogger().log(Level.INFO, String.format("Command '%s' doesn't exist !", commandLabel));
    }

    public void showHelp() {
        this.hydra.getLogger().log(Level.INFO, ChatColor.YELLOW + "-------------" + ChatColor.DARK_GREEN + " Hydra Help" + ChatColor.YELLOW + " -------------");

        for (HydraCommand command : this.commands) {
            final String help = command.getHelp();

            if (help != null) {
                this.hydra.getLogger().log(Level.INFO, String.format("%s%s: %s", ChatColor.DARK_GREEN + command.getCommandLabel(), ChatColor.WHITE, help));
            } else {
                this.hydra.getLogger().log(Level.INFO, String.format("%s: %s", command.getCommandLabel(), "this command doesn't have help text"));
            }
        }

        this.hydra.getLogger().log(Level.INFO, ChatColor.YELLOW + "--------------------------------------");
    }

    public void addCommand(HydraCommand command) {
        this.commands.add(command);
        System.out.println("Registered '" + command.getCommandLabel() + "' command");
    }

    public void removeCommand(HydraCommand command) {
        this.commands.remove(command);
    }

    public List<HydraCommand> getCommands() {
        return this.commands;
    }
}
