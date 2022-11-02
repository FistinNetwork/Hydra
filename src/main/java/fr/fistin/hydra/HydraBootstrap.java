package fr.fistin.hydra;

public class HydraBootstrap {

    public static void main(String[] args) {
        if (Float.parseFloat(System.getProperty("java.class.version")) < 62.0D) {
            System.err.println("*** ERROR *** Hydra requires Java >= 18 to work!");
            return;
        }

        new Hydra().start();
    }
}
