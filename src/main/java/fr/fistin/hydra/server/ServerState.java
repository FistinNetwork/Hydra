package fr.fistin.hydra.server;

public enum ServerState {

    CREATING(0,"Creating", false),
    STARTING(1, "Starting", false),
    READY(2, "Ready", true),
    PLAYING(3, "In Game", false),
    SHUTDOWN(4, "Shutdown", false),
    IDLE(5, "Freeze", false);

    private final int id;
    private final String displayText;
    private final boolean access;

    ServerState(int id, String displayText, boolean access) {
        this.id = id;
        this.displayText = displayText;
        this.access = access;
    }

    public int getId() {
        return this.id;
    }

    public String getDisplayText() {
        return this.displayText;
    }

    public boolean isAccessible() {
        return this.access;
    }

    @Override
    public String toString() {
        return this.getDisplayText();
    }

    public static ServerState fromInteger(Integer id) {
        for (ServerState state : ServerState.values())
            if (state.getId() == id)
                return state;

        return null;
    }
}