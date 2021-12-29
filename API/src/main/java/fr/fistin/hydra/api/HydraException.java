package fr.fistin.hydra.api;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 28/12/2021 at 20:55
 */
public class HydraException extends RuntimeException {

    /**
     * Constructor of {@link HydraException}
     *
     * @param message Message of the error
     */
    public HydraException(String message) {
        super(message);
    }

    /**
     * Constructor of {@link HydraException}
     *
     * @param message Message of the error
     * @param cause Cause of the error
     */
    public HydraException(String message, Throwable cause) {
        super(message, cause);
    }

}
