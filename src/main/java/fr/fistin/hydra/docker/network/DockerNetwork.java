package fr.fistin.hydra.docker.network;

import fr.fistin.hydra.config.HydraConfig;

public class DockerNetwork {

    public static final DockerNetwork HYDRA = new DockerNetwork(HydraConfig.get().getDocker().getStackName() + "_" + HydraConfig.get().getDocker().getNetworkName(), DockerNetworkDriver.OVERLAY);

    private final String name;
    private final DockerNetworkDriver networkDriver;

    public DockerNetwork(String name, DockerNetworkDriver networkDriver) {
        this.name = name;
        this.networkDriver = networkDriver;
    }

    public String getName() {
        return this.name;
    }

    public DockerNetworkDriver getNetworkDriver() {
        return this.networkDriver;
    }

}
