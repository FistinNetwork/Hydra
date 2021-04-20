package fr.fistin.hydra.configuration;

import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.util.logger.LogType;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import java.io.*;

public class HydraConfigurationManager {

    private HydraConfiguration configuration;

    private final Hydra hydra;
    private final File configFile;

    public HydraConfigurationManager(Hydra hydra, File configFile) {
        this.hydra = hydra;
        this.configFile = configFile;
    }

    public void loadConfiguration() {
        try {
            this.createConfiguration();

            final InputStream inputStream = new FileInputStream(this.configFile);
            final Yaml yaml = new Yaml(new Constructor(HydraConfiguration.class));

            this.hydra.getLogger().log(LogType.INFO, String.format("Loading configuration from %s file...", this.configFile.getName()));

            this.configuration = yaml.load(inputStream);
        } catch (IOException e) {
            this.hydra.getLogger().log(LogType.ERROR, String.format("An error occurred during loading configuration from %s file. Please restart Hydra.", this.configFile.getName()));
            this.configFile.delete();
            this.hydra.shutdown();
        }
    }

    private void createConfiguration() throws IOException {
         if (this.configFile.createNewFile()) {
             this.configuration = new HydraConfiguration();

             this.hydra.getLogger().log(LogType.INFO, String.format("%s file didn't exist !", this.configFile.getName()));
             this.hydra.getLogger().log(LogType.INFO, String.format("Creating %s file...!", this.configFile.getName()));

             final DumperOptions options = new DumperOptions();
             options.setIndent(2);
             options.setPrettyFlow(true);
             options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

             final Representer representer = new Representer();
             representer.addClassTag(HydraConfiguration.class, Tag.MAP);

             final PrintWriter writer = new PrintWriter(this.configFile);
             final Yaml yaml = new Yaml(representer, options);

             yaml.dump(this.configuration, writer);
             writer.close();
         }
    }

    public File getConfigFile() {
        return this.configFile;
    }

    public HydraConfiguration getConfiguration() {
        return this.configuration;
    }
}
