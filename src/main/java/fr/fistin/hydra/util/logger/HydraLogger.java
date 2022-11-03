package fr.fistin.hydra.util.logger;

import fr.fistin.hydra.util.References;
import jline.console.ConsoleReader;
import org.fusesource.jansi.Ansi;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.*;

public class HydraLogger extends Logger {

    private final Dispatcher dispatcher = new Dispatcher(this);

    private ConsoleReader consoleReader;

    @SuppressWarnings({"CallToPrintStackTrace", "CallToThreadStartDuringObjectConstruction"})
    public HydraLogger() {
        super(References.NAME, null);

        this.setLevel(Level.ALL);

        try {
            if (!Files.exists(References.LOG_FOLDER)) {
                Files.createDirectory(References.LOG_FOLDER);
            }

            this.consoleReader = new ConsoleReader();
            this.consoleReader.setExpandEvents(false);

            final FileHandler fileHandler = new FileHandler(References.LOG_FILE.toString());

            fileHandler.setFormatter(new ConciseFormatter(this, false));

            this.addHandler(fileHandler);

            final Writer consoleHandler = new Writer(consoleReader);

            consoleHandler.setLevel(Level.INFO);
            consoleHandler.setFormatter(new ConciseFormatter(this, true));

            this.addHandler(consoleHandler);
        } catch (IOException e) {
            System.err.println("Couldn't register logger!");
            System.exit(-1);
        }

        System.setErr(new PrintStream(new OutputStream(this, Level.SEVERE), true));
        System.setOut(new PrintStream(new OutputStream(this, Level.INFO), true));

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
                """
                        $$\\   $$\\                 $$\\                   \s
                        $$ |  $$ |                $$ |                  \s
                        $$ |  $$ |$$\\   $$\\  $$$$$$$ | $$$$$$\\  $$$$$$\\ \s
                        $$$$$$$$ |$$ |  $$ |$$  __$$ |$$  __$$\\ \\____$$\\\s
                        $$  __$$ |$$ |  $$ |$$ /  $$ |$$ |  \\__|$$$$$$$ |
                        $$ |  $$ |$$ |  $$ |$$ |  $$ |$$ |     $$  __$$ |
                        $$ |  $$ |\\$$$$$$$ |\\$$$$$$$ |$$ |     \\$$$$$$$ |
                        \\__|  \\__| \\____$$ | \\_______|\\__|      \\_______|
                                  $$\\   $$ |                            \s
                                  \\$$$$$$  |                            \s
                                   \\______/                             \s""";

        System.out.println(text.replaceAll("\\$", "█"));
    }

    public ConsoleReader getConsoleReader() {
        return this.consoleReader;
    }

    public Dispatcher getDispatcher() {
        return this.dispatcher;
    }

    private static class ConciseFormatter extends Formatter {

        private final Logger logger;
        private final boolean colored;

        public ConciseFormatter(Logger logger, boolean colored) {
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
                LogColor color;

                if (level == Level.INFO) {
                    color = LogColor.GREEN;
                } else if (level == Level.WARNING) {
                    color = LogColor.YELLOW;
                } else if (level == Level.SEVERE) {
                    color = LogColor.RED;
                } else {
                    color = LogColor.AQUA;
                }

                builder.append(color).append(level.getLocalizedName()).append(LogColor.RESET);
            } else {
                builder.append(level.getLocalizedName());
            }
        }
    }

    private static class Writer extends Handler {

        private final Map<LogColor, String> replacements = new EnumMap<>(LogColor.class);
        private final LogColor[] colors = LogColor.values();
        private final ConsoleReader console;

        public Writer(ConsoleReader console) {
            this.console = console;

            this.replacements.put(LogColor.BLACK, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLACK).boldOff().toString());
            this.replacements.put(LogColor.DARK_BLUE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLUE).boldOff().toString());
            this.replacements.put(LogColor.DARK_GREEN, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.GREEN).boldOff().toString());
            this.replacements.put(LogColor.DARK_AQUA, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.CYAN).boldOff().toString());
            this.replacements.put(LogColor.DARK_RED, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.RED).boldOff().toString());
            this.replacements.put(LogColor.DARK_PURPLE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.MAGENTA).boldOff().toString());
            this.replacements.put(LogColor.GOLD, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.YELLOW).boldOff().toString());
            this.replacements.put(LogColor.GRAY, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.WHITE).boldOff().toString());
            this.replacements.put(LogColor.DARK_GRAY, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLACK).bold().toString());
            this.replacements.put(LogColor.BLUE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLUE).bold().toString());
            this.replacements.put(LogColor.GREEN, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.GREEN).bold().toString());
            this.replacements.put(LogColor.AQUA, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.CYAN).bold().toString());
            this.replacements.put(LogColor.RED, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.RED).bold().toString());
            this.replacements.put(LogColor.LIGHT_PURPLE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.MAGENTA).bold().toString());
            this.replacements.put(LogColor.YELLOW, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.YELLOW).bold().toString());
            this.replacements.put(LogColor.WHITE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.WHITE).bold().toString());
            this.replacements.put(LogColor.MAGIC, Ansi.ansi().a(Ansi.Attribute.BLINK_SLOW).toString());
            this.replacements.put(LogColor.BOLD, Ansi.ansi().a(Ansi.Attribute.UNDERLINE_DOUBLE).toString());
            this.replacements.put(LogColor.STRIKETHROUGH, Ansi.ansi().a(Ansi.Attribute.STRIKETHROUGH_ON).toString());
            this.replacements.put(LogColor.UNDERLINE, Ansi.ansi().a(Ansi.Attribute.UNDERLINE).toString());
            this.replacements.put(LogColor.ITALIC, Ansi.ansi().a(Ansi.Attribute.ITALIC).toString());
            this.replacements.put(LogColor.RESET, Ansi.ansi().a(Ansi.Attribute.RESET).toString());
        }

        public void print(String s) {
            for (LogColor color : this.colors) {
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

    public class Dispatcher extends Thread {

        private final HydraLogger logger;
        private final BlockingQueue<LogRecord> queue = new LinkedBlockingQueue<>();

        public Dispatcher(HydraLogger logger) {
            super("Hylios Logger Thread");
            this.logger = logger;
        }

        @Override
        public void run() {
            while (!this.isInterrupted()) {
                LogRecord record;
                try {
                    record = this.queue.take();
                } catch (InterruptedException e) {
                    continue;
                }

                this.logger.doLog(record);
            }

            for (LogRecord record : this.queue) {
                this.logger.doLog(record);
            }

        }

        public void queue(LogRecord record) {
            if (!this.isInterrupted())
                this.queue.add(record);
        }

        public BlockingQueue<LogRecord> getQueue() {
            return this.queue;
        }
    }

    private static class OutputStream extends ByteArrayOutputStream {

        private final Logger logger;
        private final Level level;

        public OutputStream(Logger logger, Level level) {
            this.logger = logger;
            this.level = level;
        }

        @Override
        public void flush() {
            final String contents = this.toString(StandardCharsets.UTF_8);

            super.reset();

            if (!contents.isEmpty() && !contents.equals(System.getProperty("line.separator"))) {
                this.logger.logp(level, "", "", contents.substring(0, contents.length() - 1));
            }
        }

    }

}
