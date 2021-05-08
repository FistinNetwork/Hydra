package fr.fistin.hydra.command.model;

import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.command.HydraCommand;
import fr.fistin.hydra.server.HydraServer;
import fr.fistin.hydra.server.template.HydraTemplate;
import fr.fistin.hydra.util.ChatColor;

import java.util.Map;
import java.util.logging.Level;

public class ServerCommand extends HydraCommand {

    private final Hydra hydra;

    public ServerCommand(Hydra hydra, String commandLabel) {
        super(commandLabel);
        this.hydra = hydra;
    }

    @Override
    public boolean execute(String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("list")) {
                this.list();
            } else if (args[0].equalsIgnoreCase("start")) {
                if (args[1] != null) this.start(args[1]);
                else System.err.println("Usage: server <start> <type>");
            } else if (args[0].equalsIgnoreCase("stop")) {
                if (args[1] != null) this.stop(args[1]);
                else System.err.println("Usage: server <stop> <id>");
            } else if (args[0].equalsIgnoreCase("check")) {
                if (args[1] != null) this.check(args[1]);
                else System.err.println("Usage: server <check> <id>");
            }
        } else {
            System.err.println("Usage: server <list|start|stop|check>");
        }
        return true;
    }

    private void list() {
        this.hydra.getLogger().log(Level.INFO, ChatColor.YELLOW + "-------------" + ChatColor.DARK_GREEN + " Hydra Servers List" + ChatColor.YELLOW + " -------------");

        final int size = this.hydra.getServerManager().getServers().size();

        if (size == 1) {
            this.hydra.getLogger().log(Level.INFO, ChatColor.GREEN + String.valueOf(size) + ChatColor.WHITE + " servers is actually running");
        } else {
            this.hydra.getLogger().log(Level.INFO, ChatColor.GREEN + String.valueOf(size) + ChatColor.WHITE + " servers are actually running");
        }

        for (Map.Entry<String, HydraServer> entry : this.hydra.getServerManager().getServers().entrySet()) {
            this.hydra.getLogger().log(Level.INFO, ChatColor.GREEN + entry.getKey() + ChatColor.WHITE + ": " + entry.getValue().getCurrentState().getDisplayText());
        }

        this.hydra.getLogger().log(Level.INFO, ChatColor.YELLOW + "----------------------------------------------");
    }

    private void start(String arg) {
        final HydraTemplate template = this.hydra.getTemplateManager().getTemplateByName(arg.toLowerCase());

        if (template != null) {
            this.hydra.getServerManager().startServer(template);
        } else {
            this.hydra.getLogger().log(Level.SEVERE, "Couldn't find a template called: " + arg);
            this.hydra.getLogger().log(Level.INFO, ChatColor.YELLOW + "-------------" + ChatColor.DARK_GREEN + " Hydra Templates List" + ChatColor.YELLOW + " -------------");

            for (Map.Entry<String, HydraTemplate> entry: this.hydra.getTemplateManager().getTemplates().entrySet()) {
                this.hydra.getLogger().log(Level.INFO, ChatColor.WHITE + "- " + ChatColor.GREEN + entry.getValue());
            }

            this.hydra.getLogger().log(Level.INFO, ChatColor.YELLOW + "------------------------------------------------");
        }
    }

    private void stop(String arg) {
        final HydraServer server = this.hydra.getServerManager().getServerByName(arg);

        if (server != null) {
            server.stop();
        } else {
            this.hydra.getLogger().log(Level.SEVERE, "Couldn't find a server with this id: " + arg);
            this.list();
        }
    }

    private void check(String arg) {
        final HydraServer server = this.hydra.getServerManager().getServerByName(arg);

        if (server != null) {
            server.checkStatus();
            this.hydra.getLogger().log(Level.INFO, "Server status is: " + server.getCurrentState().getDisplayText());
        } else {
            this.hydra.getLogger().log(Level.SEVERE, "Couldn't find a server with this id: " + arg);
            this.list();
        }
    }

    @Override
    public String getHelp() {
        return "Get information from a server, stop a server, start a server, get all servers, etc";
    }
}
