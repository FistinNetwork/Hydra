package fr.fistin.hydra.util.logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.LogRecord;

public class LogDispatcher extends Thread {

    private final HydraLogger logger;
    private final BlockingQueue<LogRecord> queue = new LinkedBlockingQueue<>();

    public LogDispatcher(HydraLogger logger) {
        super("Hydra Logger Thread");
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
}
