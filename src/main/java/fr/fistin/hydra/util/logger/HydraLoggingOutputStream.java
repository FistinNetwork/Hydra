package fr.fistin.hydra.util.logger;

import com.google.common.base.Charsets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HydraLoggingOutputStream extends ByteArrayOutputStream {

    private final Logger logger;
    private final Level level;

    public HydraLoggingOutputStream(Logger logger, Level level) {
        this.logger = logger;
        this.level = level;
    }

    @Override
    public void flush() throws IOException {
        final String contents = this.toString(Charsets.UTF_8.name());

        super.reset();

        if (!contents.isEmpty() && !contents.equals(System.getProperty("line.separator"))) {
            this.logger.logp(level, "", "", contents.replace("\n", ""));
        }
    }

}
