package fr.fistin.hydra.util.logger;

import fr.fistin.hydra.Hydra;

import java.io.IOException;
import java.util.logging.*;

public class HydraLogger extends Logger {

    private final LogDispatcher dispatcher = new LogDispatcher(this);

    @SuppressWarnings( { "CallToPrintStackTrace", "CallToThreadStartDuringObjectConstruction" } )
    public HydraLogger(Hydra hydra, String name, String filePattern) {
        super(name, null);
        this.setLevel(Level.ALL);

        try {
            final FileHandler fileHandler = new FileHandler(filePattern);
            fileHandler.setFormatter(new ConciseFormatter(this, false));
            this.addHandler(fileHandler);


            final ColouredWriter consoleHandler = new ColouredWriter(hydra.getConsoleReader());
            consoleHandler.setLevel(Level.INFO);
            consoleHandler.setFormatter(new ConciseFormatter(this, true));
            this.addHandler(consoleHandler);

        } catch (IOException e) {
            System.err.println("Couldn't register logger !");
            hydra.shutdown();
        }
        this.dispatcher.start();
    }

    @Override
    public void log(LogRecord record) {
        this.dispatcher.queue(record);
    }

    void doLog(LogRecord record) {
        super.log(record);
    }

    public static void printHeaderMessage() {
        final String text =
                "$$\\   $$\\                 $$\\                    \n" +
                "$$ |  $$ |                $$ |                   \n" +
                "$$ |  $$ |$$\\   $$\\  $$$$$$$ | $$$$$$\\  $$$$$$\\  \n" +
                "$$$$$$$$ |$$ |  $$ |$$  __$$ |$$  __$$\\ \\____$$\\ \n" +
                "$$  __$$ |$$ |  $$ |$$ /  $$ |$$ |  \\__|$$$$$$$ |\n" +
                "$$ |  $$ |$$ |  $$ |$$ |  $$ |$$ |     $$  __$$ |\n" +
                "$$ |  $$ |\\$$$$$$$ |\\$$$$$$$ |$$ |     \\$$$$$$$ |\n" +
                "\\__|  \\__| \\____$$ | \\_______|\\__|      \\_______|\n" +
                "          $$\\   $$ |                             \n" +
                "          \\$$$$$$  |                             \n" +
                "           \\______/                              ";

        System.out.println(text.replaceAll("\\$", "█"));
    }

}
