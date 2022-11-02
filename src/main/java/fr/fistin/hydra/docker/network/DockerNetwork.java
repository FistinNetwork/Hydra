package fr.fistin.hydra.docker.network;

import fr.fistin.hydra.Hydra;

public class DockerNetwork {

    public static final DockerNetwork HYDRA = new DockerNetwork(Hydra.get().getConfig().getDocker().getStackName() + "_" + Hydra.get().getConfig().getDocker().getNetworkName(), DockerNetworkDriver.OVERLAY);

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
