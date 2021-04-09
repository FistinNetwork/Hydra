package fr.fistin.hydra.scheduler;

import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.util.logger.LogType;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class HydraScheduler {

    private final Object lock = new Object();
    private final AtomicInteger taskCounter = new AtomicInteger();
    private final Map<Integer, HydraTask> tasks = new HashMap<>();

    private final Hydra hydra;

    public HydraScheduler(Hydra hydra) {
        this.hydra = hydra;

        this.hydra.getLogger().log(LogType.INFO, "Starting hydra scheduler...");
    }

    public HydraTask schedule(Runnable task, long delay, TimeUnit unit) {
        return this.schedule(task, delay, 0, unit);
    }

    public HydraTask schedule(Runnable task, long delay, long period, TimeUnit unit) {
        final HydraTask prepared = new HydraTask(this, this.taskCounter.getAndIncrement(), task, delay, period, unit);

        synchronized (this.lock) {
            this.tasks.put(prepared.getId(), prepared);
        }

        this.hydra.getExecutorService().execute(prepared);
        return prepared;
    }

    public HydraTask runTaskAsynchronously(Runnable task) {
        return this.schedule(task, 0, TimeUnit.MILLISECONDS);
    }

    public void cancel(int id) {
        final HydraTask task = this.tasks.get(id);

        if (task != null) task.cancel();
        else System.out.printf("Couldn't find a task with id: %d", id);
    }

    void cancel0(HydraTask task) {
        synchronized (this.lock) {
            this.tasks.remove(task.getId());
        }
    }

}
