package fr.fistin.hydra.commands;

import fr.fistin.hydra.servermanager.Server;
import fr.fistin.hydra.servermanager.VanillaServer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Map;

public class baseCommand extends Command {
    public baseCommand() {
        super("hydra");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0){
            sender.sendMessage("" + ChatColor.DARK_RED + ChatColor.BOLD + "[Hydra] "+ ChatColor.RESET + ChatColor.GRAY + "Merci de donner une commande");
        } else {
            switch(args[0]){
                case "whereami": {
                    ProxiedPlayer player = ProxyServer.getInstance().getPlayer(sender.getName());
                    sender.sendMessage("" + ChatColor.DARK_RED + ChatColor.BOLD + "[Hydra] "+ ChatColor.RESET + ChatColor.GRAY + player.getServer().getInfo());
                    break;
                }

                case "docker": {
                    VanillaServer vanillaServer = new VanillaServer();
                    vanillaServer.start();
                    sender.sendMessage("" + ChatColor.DARK_RED + ChatColor.BOLD + "[Hydra] "+ ChatColor.RESET + ChatColor.GRAY + vanillaServer);
                    sender.sendMessage("" + ChatColor.DARK_RED + ChatColor.BOLD + "[Hydra] "+ ChatColor.RESET + ChatColor.GRAY + "Nom du serveur : " + vanillaServer.getType());
                    sender.sendMessage("" + ChatColor.DARK_RED + ChatColor.BOLD + "[Hydra] "+ ChatColor.RESET + ChatColor.GRAY + "IP du serveur : " + vanillaServer.getServerIP());
                    break;
                }

                case "info": {
                    if(args.length < 2){
                        sender.sendMessage("" + ChatColor.DARK_RED + ChatColor.BOLD + "[Hydra] "+ ChatColor.RESET + ChatColor.GRAY + "Vous avez oublié le nom du serveur");
                        return;
                    }
                    Server server = Server.getServerByName(args[1]);
                    if(server == null)
                        sender.sendMessage("" + ChatColor.DARK_RED + ChatColor.BOLD + "[Hydra] "+ ChatColor.RESET + ChatColor.GRAY + "Le serveur n'existe pas !");
                    else {
                        sender.sendMessage("" + ChatColor.DARK_RED + ChatColor.BOLD + "[Hydra] "+ ChatColor.RESET + ChatColor.GRAY + "======= " + server.toString() + " =======");
                        sender.sendMessage("" + ChatColor.DARK_RED + ChatColor.BOLD + "[Hydra] "+ ChatColor.RESET + ChatColor.GRAY + "Type : " + server.getType());
                        sender.sendMessage("" + ChatColor.DARK_RED + ChatColor.BOLD + "[Hydra] "+ ChatColor.RESET + ChatColor.GRAY + "État : " + ChatColor.BOLD + server.getCurrentState().toString());
                        sender.sendMessage("" + ChatColor.DARK_RED + ChatColor.BOLD + "[Hydra] "+ ChatColor.RESET + ChatColor.GRAY + "IP : " + server.getServerIP() + ":" + server.getServerPort());
                        sender.sendMessage("" + ChatColor.DARK_RED + ChatColor.BOLD + "[Hydra] "+ ChatColor.RESET + ChatColor.GRAY + "Slots :" + server.getCurrentPlayers() + "/" + server.getSlots());
                        TextComponent join = new TextComponent("" + ChatColor.DARK_RED + ChatColor.BOLD + "[Hydra] "+ ChatColor.RESET + ChatColor.BOLD + "Rejoindre le serveur");
                        join.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GOLD + "Cliquer ici pour rejoindre !").create()));
                        join.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/server " + server.toString()));
                        sender.sendMessage(join);
                        sender.sendMessage("" + ChatColor.DARK_RED + ChatColor.BOLD + "[Hydra] "+ ChatColor.RESET + ChatColor.GRAY + "=======" + new String(new char[server.toString().length()]).replace("\0", "=") + "=======");
                    }
                    break;
                }

                case "list": {
                    if(Server.getServerList().size() == 0){
                        sender.sendMessage("" + ChatColor.DARK_RED + ChatColor.BOLD + "[Hydra] "+ ChatColor.RESET + ChatColor.GRAY + "Il n'y encore aucun serveur");
                    }

                    sender.sendMessage("" + ChatColor.DARK_RED + ChatColor.BOLD + "[Hydra] "+ ChatColor.RESET + ChatColor.GRAY + "Liste des serveurs");
                    TextComponent result = new TextComponent();
                    result.addExtra(new TextComponent("" + ChatColor.DARK_RED + ChatColor.BOLD + "[Hydra] "+ ChatColor.RESET + ChatColor.BOLD));
                    for(Map.Entry<String, Server> server: Server.getServerList().entrySet()){
                        TextComponent textComponent = new TextComponent(server.getValue().getCurrentState().toColor() + server.getKey());
                        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Obtenir plus d'informations").create()));
                        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hydra info " + server.getKey()));
                        result.addExtra(textComponent);
                        result.addExtra(new TextComponent(ChatColor.GRAY + ", "));
                    }
                    sender.sendMessage(result);
                    break;
                }

                case "stop": {
                    if(args.length < 2){
                        sender.sendMessage("" + ChatColor.DARK_RED + ChatColor.BOLD + "[Hydra] "+ ChatColor.RESET + ChatColor.GRAY + "Vous avez oublié le nom du serveur");
                        return;
                    }
                    Server server = Server.getServerByName(args[1]);
                    if(server == null)
                        sender.sendMessage("" + ChatColor.DARK_RED + ChatColor.BOLD + "[Hydra] "+ ChatColor.RESET + ChatColor.GRAY + "Le serveur n'existe pas !");
                    else {
                        server.stop();
                    }
                    break;
                }

                default: {
                    sender.sendMessage("" + ChatColor.DARK_RED + ChatColor.BOLD + "[Hydra] "+ ChatColor.RESET + ChatColor.GRAY + "Merci de donner une commande");
                    break;
                }
            }
        }

    }
}
