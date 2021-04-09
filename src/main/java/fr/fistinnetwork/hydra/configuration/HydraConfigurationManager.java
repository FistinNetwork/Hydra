package fr.fistinnetwork.hydra.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import fr.fistinnetwork.hydra.Hydra;
import fr.fistinnetwork.hydra.util.logger.LogType;

import java.io.File;
import java.io.IOException;

public class HydraConfigurationManager {

    private HydraConfiguration configuration;

    private final ObjectMapper mapper;

    private final Hydra hydra;
    private final File configFile;

    public HydraConfigurationManager(Hydra hydra, File configFile) {
        this.hydra = hydra;
        this.configFile = configFile;
        this.mapper = new ObjectMapper(new YAMLFactory());
    }

    public void loadConfiguration() {
        try {
            this.createConfiguration();

            this.hydra.getLogger().log(LogType.INFO, String.format("Loading configuration from %s file...", this.configFile.getName()));

            this.configuration = this.mapper.readValue(this.configFile, HydraConfiguration.class);
        } catch (IOException e) {
            this.hydra.getLogger().log(LogType.ERROR, String.format("An error occurred during loading configuration from %s file. Please restart Hydra.", this.configFile.getName()));
            this.configFile.delete();
            this.hydra.shutdown();
        }
    }

    private void createConfiguration() throws IOException {
         if (this.configFile.createNewFile()) {
             this.configuration = new HydraConfiguration();

             this.hydra.getLogger().log(LogType.INFO, String.format("%s folder didn't exist !", this.configFile.getName()));
             this.hydra.getLogger().log(LogType.INFO, String.format("Creating %s file...!", this.configFile.getName()));

             this.mapper.writeValue(this.configFile, this.configuration);
         }
    }

    public File getConfigFile() {
        return this.configFile;
    }

    public HydraConfiguration getConfiguration() {
        return this.configuration;
    }
}
