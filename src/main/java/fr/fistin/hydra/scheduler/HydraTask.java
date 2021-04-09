package fr.fistin.hydra.scheduler;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class HydraTask implements Runnable {

    private final AtomicBoolean running = new AtomicBoolean(true);

    private final HydraScheduler scheduler;
    private final int id;
    private final Runnable task;
    private final long delay;
    private final long period;

    public HydraTask(HydraScheduler scheduler, int id, Runnable task, long delay, long period, TimeUnit unit) {
        this.scheduler = scheduler;
        this.id = id;
        this.task = task;
        this.delay = unit.toMillis(delay);
        this.period = unit.toMillis(period);
    }

    @Override
    public void run() {
        if (delay > 0 ) {
            try {
                Thread.sleep(this.delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        while (this.running.get()) {
            this.task.run();

            if (this.period <= 0) break;

            try {
                Thread.sleep(this.period);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        this.cancel();
    }

    public void cancel() {
        this.scheduler.cancel0(this);
    }

    public int getId() {
        return this.id;
    }
}
