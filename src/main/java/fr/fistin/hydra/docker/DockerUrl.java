package fr.fistin.hydra.docker;

public enum DockerUrl {

    UNIX("unix:///var/run/docker.sock"),
    WINDOWS("tcp://localhost:2375");

    private final String url;

    DockerUrl(String url) {
        this.url = url;
    }

    public static DockerUrl get() {
        if(System.getProperty("os.name").toLowerCase().contains("win")) return WINDOWS;
        else return UNIX;
    }

    public String getUrl() {
        return this.url;
    }

    @Override
    public String toString() {
        return this.url;
    }
}
