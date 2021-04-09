package fr.fistin.hydra.util.logger;

public class LogType {

    public static final LogType INFO = new LogType("info");
    public static final LogType WARN = new LogType("warn");
    public static final LogType ERROR = new LogType("error");

    private String name;

    public LogType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
