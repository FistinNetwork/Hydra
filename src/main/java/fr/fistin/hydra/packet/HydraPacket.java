package fr.fistin.hydra.packet;

import fr.fistin.hydra.util.References;

import java.util.UUID;

public class HydraPacket {

    private final String type;
    private final String id;
    private final String name;

    public HydraPacket(String type) {
        this.type = type;
        this.id = UUID.randomUUID().toString().split("-")[0];
        this.name = "hydra-" + this.type + "-" + this.id;
    }

    public String getType() {
        return this.type;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String toJson() {
        return References.GSON.toJson(this);
    }

}
