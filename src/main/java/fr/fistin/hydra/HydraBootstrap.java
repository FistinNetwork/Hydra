package fr.fistin.hydra;

import fr.fistin.hydra.server.HydraServer;
import fr.fistin.hydraconnector.api.ServerState;
import fr.fistin.hydraconnector.protocol.channel.HydraChannel;
import fr.fistin.hydraconnector.protocol.packet.server.UpdateServerInfoPacket;

import java.util.concurrent.TimeUnit;

public class HydraBootstrap {

    public static void main(String[] args) {
        if (Float.parseFloat(System.getProperty("java.class.version")) < 52.0D) {
            System.err.println("*** ERROR *** Hydra requires Java >= 8 to function!");
            return;
        }

        final Hydra hydra = new Hydra();
        hydra.start();

        hydra.getScheduler().schedule(() -> {
            HydraServer server = null;
            for (HydraServer s : hydra.getServerManager().getServers().values()) {
                server = s;
            }

            hydra.getHydraConnector().getConnectionManager().sendPacket(HydraChannel.SERVERS, new UpdateServerInfoPacket(server.toString(), 10, ServerState.PLAYING));
        }, 120, TimeUnit.SECONDS);
    }
}
