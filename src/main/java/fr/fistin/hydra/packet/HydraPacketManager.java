package fr.fistin.hydra.packet;

import java.util.HashMap;
import java.util.Map;

public class HydraPacketManager {

    private final Map<String, Class<? extends HydraPacket>> packetClassMap = new HashMap<>();

    public void registerPacket(String type, Class<? extends HydraPacket> packetClass) {
        this.packetClassMap.putIfAbsent(type, packetClass);
    }

    public String getPacketIdByClass(Class<? extends HydraPacket> packetClass) {
        for (Map.Entry<String, Class<? extends HydraPacket>> entry : this.packetClassMap.entrySet()) {
            if (packetClass == entry.getValue()) {
                return entry.getKey();
            }
        }
        return null;
    }

    public Class<? extends HydraPacket> getPacketClassByType(String type) {
        return this.packetClassMap.get(type);
    }

}
