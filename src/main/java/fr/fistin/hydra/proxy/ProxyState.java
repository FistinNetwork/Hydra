package fr.fistin.hydra.proxy;

public enum ProxyState {

    CREATING(0, "Création", false),
    STARTING(1, "Démarrage", false),
    READY(2, "Prêt", true),
    SHUTDOWN(3, "Arrêt", false),
    IDLE(4, "Freeze", false);

    private final int id;
    private final String displayText;
    private final boolean access;

    ProxyState(int id, String displayText, boolean access) {
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

    public static ProxyState fromInteger(Integer id) {
        for (ProxyState state : ProxyState.values())
            if (state.getId() == id)
                return state;

        return null;
    }
}
