package fr.fistin.hydra.configuration;

import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.util.References;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;

public class HydraConfigurationManager {

    private HydraConfiguration configuration;

    private final Hydra hydra;

    public HydraConfigurationManager(Hydra hydra) {
        this.hydra = hydra;
    }

    public void loadConfiguration() {
        try {
            this.createConfiguration();

            final InputStream inputStream = Files.newInputStream(References.CONFIG_FILE);
            final Yaml yaml = new Yaml(new Constructor(HydraConfiguration.class));

            this.hydra.getLogger().log(Level.INFO, String.format("Loading configuration from %s file...", References.CONFIG_FILE.getFileName()));

            this.configuration = yaml.load(inputStream);
        } catch (IOException e) {
            this.hydra.getLogger().log(Level.SEVERE, String.format("An error occurred during loading configuration from %s file. Please restart %s.", References.CONFIG_FILE.getFileName(), References.NAME));

            try {
                Files.delete(References.CONFIG_FILE);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            this.hydra.shutdown();
        }
    }

    private void createConfiguration() throws IOException {
         if (!Files.exists(References.CONFIG_FILE)) {
             this.configuration = new HydraConfiguration();

             this.hydra.getLogger().log(Level.INFO, String.format("%s file doesn't exist !", References.CONFIG_FILE.getFileName()));
             this.hydra.getLogger().log(Level.INFO, String.format("Creating %s file...!", References.CONFIG_FILE.getFileName()));

             Files.createFile(References.CONFIG_FILE);

             final DumperOptions options = new DumperOptions();
             options.setIndent(2);
             options.setPrettyFlow(true);
             options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

             final Representer representer = new Representer();
             representer.addClassTag(HydraConfiguration.class, Tag.MAP);

             final PrintWriter writer = new PrintWriter(Files.newOutputStream(References.CONFIG_FILE));
             final Yaml yaml = new Yaml(representer, options);

             yaml.dump(this.configuration, writer);
             writer.close();
         }
    }

    public HydraConfiguration getConfiguration() {
        return this.configuration;
    }

}
