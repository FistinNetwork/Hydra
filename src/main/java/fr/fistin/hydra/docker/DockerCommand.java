package fr.fistin.hydra.docker;

public class DockerCommand {

    private final String name;
    private final Runnable task;

    public DockerCommand(String name, Runnable task) {
        this.name = name;
        this.task = task;
    }

    public String getName() {
        return this.name;
    }

    public Runnable getTask() {
        return this.task;
    }

}
