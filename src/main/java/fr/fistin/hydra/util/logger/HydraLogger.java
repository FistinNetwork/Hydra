package fr.fistin.hydra.util.logger;

import fr.fistin.hydra.Hydra;
import jline.console.ConsoleReader;
import org.fusesource.jansi.Ansi;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.EnumMap;
import java.util.Map;
import java.util.logging.*;

public class HydraLogger extends Logger {

    private final HydraLogDispatcher dispatcher = new HydraLogDispatcher(this);

    @SuppressWarnings( { "CallToPrintStackTrace", "CallToThreadStartDuringObjectConstruction" } )
    public HydraLogger(Hydra hydra, String name, String filePattern) {
        super(name, null);
        this.setLevel(Level.ALL);

        try {
            final FileHandler fileHandler = new FileHandler(filePattern);
            fileHandler.setFormatter(new HydraConciseFormatter(this, false));
            this.addHandler(fileHandler);


            final HydraColouredWriter consoleHandler = new HydraColouredWriter(hydra.getConsoleReader());
            consoleHandler.setLevel(Level.INFO);
            consoleHandler.setFormatter(new HydraConciseFormatter(this, true));
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

    private static class HydraConciseFormatter extends Formatter {

        private final Logger logger;
        private final boolean colored;

        public HydraConciseFormatter(Logger logger, boolean colored) {
            this.logger = logger;
            this.colored = colored;
        }

        @Override
        @SuppressWarnings("ThrowableResultIgnored")
        public String format(LogRecord record) {
            final StringBuilder formatted = new StringBuilder();

            formatted.append("[")
                    .append(this.logger.getName())
                    .append("] ")
                    .append("[");
            this.appendLevel(formatted, record.getLevel());
            formatted.append("] ")
                    .append(this.formatMessage(record))
                    .append("\n");

            if (record.getThrown() != null) {
                final StringWriter writer = new StringWriter();
                record.getThrown().printStackTrace(new PrintWriter(writer));
                formatted.append(writer);
            }

            return formatted.toString();
        }

        private void appendLevel(StringBuilder builder, Level level) {
            if (this.colored) {
                HydraLogColor color;

                if (level == Level.INFO) {
                    color = HydraLogColor.GREEN;
                } else if (level == Level.WARNING) {
                    color = HydraLogColor.YELLOW;
                } else if (level == Level.SEVERE) {
                    color = HydraLogColor.RED;
                } else {
                    color = HydraLogColor.AQUA;
                }

                builder.append(color).append(level.getName()).append(HydraLogColor.RESET);
            } else {
                builder.append(level.getName());
            }
        }
    }

    private static class HydraColouredWriter extends Handler {

        private final Map<HydraLogColor, String> replacements = new EnumMap<>(HydraLogColor.class);
        private final HydraLogColor[] colors = HydraLogColor.values();
        private final ConsoleReader console;

        public HydraColouredWriter(ConsoleReader console) {
            this.console = console;

            this.replacements.put(HydraLogColor.BLACK, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLACK).boldOff().toString());
            this.replacements.put(HydraLogColor.DARK_BLUE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLUE).boldOff().toString());
            this.replacements.put(HydraLogColor.DARK_GREEN, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.GREEN).boldOff().toString());
            this.replacements.put(HydraLogColor.DARK_AQUA, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.CYAN).boldOff().toString());
            this.replacements.put(HydraLogColor.DARK_RED, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.RED).boldOff().toString());
            this.replacements.put(HydraLogColor.DARK_PURPLE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.MAGENTA).boldOff().toString());
            this.replacements.put(HydraLogColor.GOLD, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.YELLOW).boldOff().toString());
            this.replacements.put(HydraLogColor.GRAY, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.WHITE).boldOff().toString());
            this.replacements.put(HydraLogColor.DARK_GRAY, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLACK).bold().toString());
            this.replacements.put(HydraLogColor.BLUE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLUE).bold().toString());
            this.replacements.put(HydraLogColor.GREEN, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.GREEN).bold().toString());
            this.replacements.put(HydraLogColor.AQUA, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.CYAN).bold().toString());
            this.replacements.put(HydraLogColor.RED, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.RED).bold().toString());
            this.replacements.put(HydraLogColor.LIGHT_PURPLE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.MAGENTA).bold().toString());
            this.replacements.put(HydraLogColor.YELLOW, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.YELLOW).bold().toString());
            this.replacements.put(HydraLogColor.WHITE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.WHITE).bold().toString());
            this.replacements.put(HydraLogColor.MAGIC, Ansi.ansi().a(Ansi.Attribute.BLINK_SLOW).toString());
            this.replacements.put(HydraLogColor.BOLD, Ansi.ansi().a(Ansi.Attribute.UNDERLINE_DOUBLE).toString());
            this.replacements.put(HydraLogColor.STRIKETHROUGH, Ansi.ansi().a(Ansi.Attribute.STRIKETHROUGH_ON).toString());
            this.replacements.put(HydraLogColor.UNDERLINE, Ansi.ansi().a(Ansi.Attribute.UNDERLINE).toString());
            this.replacements.put(HydraLogColor.ITALIC, Ansi.ansi().a(Ansi.Attribute.ITALIC).toString());
            this.replacements.put(HydraLogColor.RESET, Ansi.ansi().a(Ansi.Attribute.RESET).toString());
        }

        public void print(String s) {
            for (HydraLogColor color : this.colors) {
                s = s.replaceAll("(?i)" + color, this.replacements.get(color));
            }

            try {
                this.console.print(ConsoleReader.RESET_LINE + s + Ansi.ansi().reset());
                this.console.drawLine();
                this.console.flush();
            } catch (IOException ignored) {}
        }

        @Override
        public void publish(LogRecord record) {
            if (this.isLoggable(record)) {
                this.print(this.getFormatter().format(record));
            }
        }

        @Override
        public void flush() {}

        @Override
        public void close() throws SecurityException {}

    }

}
