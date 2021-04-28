package fr.fistin.hydra;

import fr.fistin.hydra.packet.channel.HydraChannel;
import fr.fistin.hydra.packet.model.query.StartServerPacket;

public class HydraBootstrap {

    public static void main(String[] args) {
        if (Float.parseFloat(System.getProperty("java.class.version")) < 52.0D) {
            System.err.println("*** ERROR *** Hydra requires Java >= 8 to function!");
            return;
        }

        final Hydra hydra = new Hydra();
        hydra.start();

        // Personal test of Hydra
        hydra.getRedisChannelsHandler().sendPacket(HydraChannel.QUERY, new StartServerPacket("StartServer", "lobby"));
    }
}
