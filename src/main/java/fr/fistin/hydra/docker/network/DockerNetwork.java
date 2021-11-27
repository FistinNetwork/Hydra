package fr.fistin.hydra.docker.network;

import fr.fistin.hydra.util.References;

public class DockerNetwork {

    public static final DockerNetwork FISTIN_NETWORK = new DockerNetwork(References.STACK_NAME + "_" + System.getenv("NETWORK_NAME"), DockerNetworkDriver.OVERLAY);

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
