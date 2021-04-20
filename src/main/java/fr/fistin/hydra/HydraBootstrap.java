package fr.fistin.hydra;

import fr.fistin.hydra.server.ServerOptions;
import fr.fistin.hydra.server.template.HydraTemplate;
import fr.fistin.hydra.server.template.TemplateDependencies;
import fr.fistin.hydra.server.template.TemplateHydraOptions;
import fr.fistin.hydra.server.template.TemplateStartingOptions;

import java.io.File;
public class HydraBootstrap {

    public static void main(String[] args) {
        if (Float.parseFloat(System.getProperty("java.class.version")) < 52.0D) {
            System.err.println("*** ERROR *** Hydra requires Java >= 8 to function!");
            return;
        }

        final Hydra hydra = new Hydra();
        hydra.start();

        // Test
        hydra.getTemplateManager().createTemplate(
                new HydraTemplate("lobby", "public",
                new TemplateStartingOptions(0, 100),
                new TemplateDependencies("https://fistincdn.blob.core.windows.net/serverdata/lobby/world.zip", "https://fistincdn.blob.core.windows.net/serverdata/lobby/plugins.zip"),
                new TemplateHydraOptions(10, new ServerOptions(), 1000)),
                new File("src/main/resources/templates/lobby.yml"));

        final HydraTemplate template = hydra.getTemplateManager().loadTemplateFromFile(new File("src/main/resources/templates/lobby.yml"));
        System.out.println(template.getName());
        System.out.println(template.getDependencies().getPluginUrl());
    }
}
