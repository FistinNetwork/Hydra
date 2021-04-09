package fr.fistin.hydra;

import fr.fistin.hydra.server.models.LobbyServer;

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

        final LobbyServer lobbyServer = new LobbyServer(hydra);
        lobbyServer.start();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                hydra.shutdown();
            }
        }, 15000);
    }

}
