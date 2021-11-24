package fr.fistin.hydra.api.server;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 24/11/2021 at 18:26
 */
public enum HydraServerState {

    /** Server is creating (when docker just created it) */
    CREATING("Création"),

    /** Server is starting (when the onEnable method is fired in plugin) */
    STARTING("Démarrage"),

    /** Server is ready to support players */
    READY("Prêt"),

    /** Server is currently running a game */
    PLAYING("En jeu"),

    /** Server is stopping */
    SHUTDOWN("Arrêt"),

    /** Server is idling (an error occurred or only freezing) */
    IDLE("Freeze");

    /** State display */
    private final String display;

    /**
     * Constructor of {@link HydraServerState}
     *
     * @param display State display
     */
    HydraServerState(String display) {
        this.display = display;
    }

    /**
     * Get state display
     *
     * @return State display
     */
    public String getDisplay() {
        return this.display;
    }

}
