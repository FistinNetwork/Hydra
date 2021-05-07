package fr.fistin.hydraconnector.protocol.channel;

public enum HydraChannel {

    DEFAULT("Hydra"),
    SERVERS("HydraServers"),
    QUERY("HydraQuery"),
    PROXIES("HydraProxies"),
    EVENT("HydraEvent");

    private final String name;

    HydraChannel(String name) {
        this.name = name;
    }

    public static HydraChannel getChannelByName(String name) {
        for (HydraChannel channel : values()) if (channel.getName().equals(name)) return channel;
        return null;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
