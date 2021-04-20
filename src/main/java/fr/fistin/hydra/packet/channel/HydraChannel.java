package fr.fistin.hydra.packet.channel;

public enum HydraChannel {

    SERVERS("HydraServers"),
    QUERY("HydraQuery"),
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
