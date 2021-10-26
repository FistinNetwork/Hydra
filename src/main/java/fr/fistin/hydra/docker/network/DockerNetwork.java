package fr.fistin.hydra.docker.network;

public class DockerNetwork {

    public static final DockerNetwork FISTIN_NETWORK = new DockerNetwork("fistin", DockerNetworkDriver.OVERLAY);

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
