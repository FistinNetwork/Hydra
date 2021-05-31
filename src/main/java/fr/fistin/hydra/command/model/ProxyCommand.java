package fr.fistin.hydra.command.model;

import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.command.HydraCommand;
import fr.fistin.hydra.proxy.HydraProxy;
import fr.fistin.hydra.proxy.HydraProxyManager;
import fr.fistin.hydra.server.HydraServer;
import fr.fistin.hydra.util.ChatColor;

import java.util.Map;
import java.util.logging.Level;

public class ProxyCommand extends HydraCommand {

    private final Hydra hydra;

    public ProxyCommand(Hydra hydra, String commandLabel) {
        super(commandLabel);
        this.hydra = hydra;
    }

    @Override
    public boolean execute(String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("list")) this.list();
            else if (args[0].equalsIgnoreCase("start")) {
                this.start();
            } else if (args[0].equalsIgnoreCase("stop")) {
                if (args.length > 1 ) {
                    if (args[1] != null) this.stop(args[1]);
                }
                else System.err.println("Usage: proxy <stop> <id>");
            } else if (args[0].equalsIgnoreCase("check")) {
                if (args.length > 1) {
                    if (args[1] != null) this.check(args[1]);
                }
            }
                else System.err.println("Usage: proxy <check> <id>");
        } else {
            System.err.println("Usage: proxy <list|start|stop|check>");
        }
        return true;
    }

    private void list() {
        this.hydra.getLogger().log(Level.INFO, ChatColor.YELLOW + "-------------" + ChatColor.DARK_GREEN + " Hydra Proxies List" + ChatColor.YELLOW + " -------------");

        final int size = this.hydra.getProxyManager().getProxies().size();

        if (size == 1) {
            this.hydra.getLogger().log(Level.INFO, ChatColor.GREEN + String.valueOf(size) + ChatColor.WHITE + " proxies is currently running");
        } else {
            this.hydra.getLogger().log(Level.INFO, ChatColor.GREEN + String.valueOf(size) + ChatColor.WHITE + " proxies are currently running");
        }

        for (Map.Entry<String, HydraProxy> entry : this.hydra.getProxyManager().getProxies().entrySet()) {
            this.hydra.getLogger().log(Level.INFO, ChatColor.GREEN + entry.getKey() + ChatColor.WHITE + ": " + entry.getValue().getCurrentState().getDisplayText());
        }

        this.hydra.getLogger().log(Level.INFO, ChatColor.YELLOW + "----------------------------------------------");
    }

    private void start() {
        this.hydra.getProxyManager().stopProxy(HydraProxyManager.defaultProxy);
    }

    private void stop(String proxyId) {
        final HydraProxy proxy = this.hydra.getProxyManager().getProxyByName(proxyId);

        if (proxy != null) {
            proxy.stop();
        } else {
            this.hydra.getLogger().log(Level.SEVERE, "Couldn't find a proxy with this id: " + proxyId);
            this.list();
        }
    }

    private void check(String proxyId) {
        final HydraProxy proxy = this.hydra.getProxyManager().getProxyByName(proxyId);

        if (proxy != null) {
            proxy.checkStatus();
            this.hydra.getLogger().log(Level.INFO, "Proxy status is: " + proxy.getCurrentState().getDisplayText());
        } else {
            this.hydra.getLogger().log(Level.SEVERE, "Couldn't find a proxy with this id: " + proxyId);
            this.list();
        }
    }

    @Override
    public String getHelp() {
        return "Get information from a proxy, stop a proxy, start a proxy, get all proxies, etc";
    }
}
