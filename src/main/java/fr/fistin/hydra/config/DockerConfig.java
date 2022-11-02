package fr.fistin.hydra.config;

/**
 * Created by AstFaster
 * on 04/06/2022 at 23:29
 */
public class DockerConfig {

    private final String stackName;
    private final String networkName;
    private final String dataFolder;

    public DockerConfig(String stackName, String networkName, String dataFolder) {
        this.stackName = stackName;
        this.networkName = networkName;
        this.dataFolder = dataFolder;
    }

    public String getStackName() {
        return this.stackName;
    }

    public String getNetworkName() {
        return this.networkName;
    }

    public String getDataFolder() {
        return this.dataFolder;
    }

}
