package fr.fistin.hydra;

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

        // Test
        final LobbyServer lobbyServer = new LobbyServer(hydra);
        lobbyServer.start();

        final VanillaServer vanillaServer = new VanillaServer(hydra);
        vanillaServer.start();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                hydra.shutdown();
            }
        }, 5000);
    }

}
