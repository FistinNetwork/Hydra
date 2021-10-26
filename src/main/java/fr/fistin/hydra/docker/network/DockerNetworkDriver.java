package fr.fistin.hydra.docker.network;

public enum DockerNetworkDriver {

    BRIDGE("bridge"),
    HOST("host"),
    OVERLAY("overlay"),
    MAC_VLAN("macvlan");

    private final String name;

    DockerNetworkDriver(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}
