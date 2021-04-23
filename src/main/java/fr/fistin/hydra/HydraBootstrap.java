package fr.fistin.hydra;

import fr.fistin.hydra.packet.channel.HydraChannel;
import fr.fistin.hydra.packet.model.StartServerPacket;
import fr.fistin.hydra.server.ServerOptions;
import fr.fistin.hydra.server.template.HydraTemplate;
import fr.fistin.hydra.server.template.TemplateDependencies;
import fr.fistin.hydra.server.template.TemplateHydraOptions;
import fr.fistin.hydra.server.template.TemplateStartingOptions;

public class HydraBootstrap {

    public static void main(String[] args) {
        if (Float.parseFloat(System.getProperty("java.class.version")) < 52.0D) {
            System.err.println("*** ERROR *** Hydra requires Java >= 8 to function!");
            return;
        }

        final Hydra hydra = new Hydra();
        hydra.start();

        // TEST
        final HydraTemplate lobbyTemplate = new HydraTemplate(
                "lobby",
                "public",
                new TemplateStartingOptions(0, -1),
                new TemplateDependencies(
                        "https://fistincdn.blob.core.windows.net/serverdata/lobby/world.zip",
                        "https://fistincdn.blob.core.windows.net/serverdata/lobby/plugins.zip"),
                new TemplateHydraOptions(10, new ServerOptions(), 1000));

        final HydraTemplate vanillaTemplate = new HydraTemplate(
                "vanilla",
                "private",
                new TemplateStartingOptions(0, -1),
                new TemplateDependencies(null, null),
                new TemplateHydraOptions(50, new ServerOptions(), 1000));

        hydra.getTemplateManager().addTemplate(lobbyTemplate);
        hydra.getTemplateManager().addTemplate(vanillaTemplate);

        hydra.getPacketManager().registerPacket("start-server", StartServerPacket.class);
        hydra.getRedisChannelsHandler().sendPacket(HydraChannel.QUERY, new StartServerPacket("start-server", "lobby"));

    }
}
