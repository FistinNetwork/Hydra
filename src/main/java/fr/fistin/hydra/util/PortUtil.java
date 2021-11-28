package fr.fistin.hydra.util;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 27/11/2021 at 21:29
 */
public class PortUtil {

    public static int nextAvailablePort(int from, int to) {
        int port = randomPort(from, to);

        while (!isPortAvailable(port)) {
            port = randomPort(from, to);
        }

        return port;
    }

    private static int randomPort(int from, int to) {
        return ThreadLocalRandom.current().nextInt(from, to);
    }

    public static boolean isPortAvailable(int port) {
        try {
            new ServerSocket(port).close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }


}
