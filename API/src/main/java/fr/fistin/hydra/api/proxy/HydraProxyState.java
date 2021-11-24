package fr.fistin.hydra.api.proxy;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 24/11/2021 at 18:24
 */
public enum HydraProxyState {

    /** Proxy is creating (when docker just created it) */
    CREATING("Création"),

    /** Proxy is starting (when the onEnable method is fired in plugin) */
    STARTING("Démarrage"),

    /** Proxy is ready to support players */
    READY("Prêt"),

    /** Proxy is stopping */
    SHUTDOWN("Arrêt"),

    /** Proxy is idling (an error occurred or only freezing) */
    IDLE("Freeze");

    /** State display */
    private final String display;

    /**
     * Constructor of {@link HydraProxyState}
     *
     * @param display State display
     */
    HydraProxyState(String display) {
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
