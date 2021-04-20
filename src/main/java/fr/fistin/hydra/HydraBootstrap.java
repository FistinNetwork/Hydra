package fr.fistin.hydra;

import fr.fistin.hydra.server.template.HydraTemplate;

public class HydraBootstrap {

    public static void main(String[] args) {
        if (Float.parseFloat(System.getProperty("java.class.version")) < 52.0D) {
            System.err.println("*** ERROR *** Hydra requires Java >= 8 to function!");
            return;
        }

        final Hydra hydra = new Hydra();
        hydra.start();

        // TEST

        /*final HydraTemplate lobbyTemplate = new HydraTemplate(
                "lobby",
                "public",
                new TemplateStartingOptions(0, -1),
                new TemplateDependencies(
                        "https://fistincdn.blob.core.windows.net/serverdata/lobby/world.zip",
                        "https://fistincdn.blob.core.windows.net/serverdata/lobby/plugins.zip"),
                new TemplateHydraOptions(50, new ServerOptions(), 1000));

        hydra.getTemplateManager().addTemplate(lobbyTemplate);*/

        final HydraTemplate lobbyTemplate = hydra.getTemplateManager().getTemplateByName("lobby");
        System.out.println(lobbyTemplate.getName());
        System.out.println(lobbyTemplate.getConfidentialityLevel());

    }
}
