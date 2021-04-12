package fr.fistin.hydra;

import fr.fistin.hydra.packet.HydraPacket;
import fr.fistin.hydra.packet.PacketReceiver;
import fr.fistin.hydra.server.models.LobbyServer;
import fr.fistin.hydra.server.models.VanillaServer;

import java.util.Timer;
import java.util.TimerTask;

public class HydraBootstrap {

    public static void main(String[] args) {
        if (Float.parseFloat(System.getProperty("java.class.version")) < 52.0D) {
            System.err.println("*** ERROR *** Hydra requires Java >= 8 to function!");
            return;
        }

        final Hydra hydra = new Hydra();
        hydra.start();

        // Test Packet System
        hydra.getPacketManager().registerPacket("packet", HydraPacket.class);
        hydra.getRedisChannelsHandler().registerPacketReceiver("test", packet -> System.out.println(packet.getId()));
        hydra.getRedisChannelsHandler().sendPacket("test", new HydraPacket("packet"));
    }

}
