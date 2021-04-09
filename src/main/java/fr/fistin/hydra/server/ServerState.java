package fr.fistin.hydra.server;

public enum ServerState {

    CREATING(0,"Création", false),
    STARTING(1, "Démarrage", false),
    READY(2, "Prêt", true),
    PLAYING(3, "En jeu", false),
    SHUTDOWN(4, "Arrêt", false),
    IDLE(5, "Freeze", false);

    private final int id;
    // TODO Translate
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