package fr.fistin.hydra.api.protocol.response;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 27/11/2021 at 15:05
 */
public class HydraResponse {

    /** Response's type */
    private HydraResponseType type;

    /** Response's message */
    private String message;

    /**
     * Constructor of {@link HydraResponse}
     *
     * @param type Type
     * @param message Message
     */
    public HydraResponse(HydraResponseType type, String message) {
        this.type = type;
        this.message = message;
    }

    /**
     * Constructor of {@link HydraResponse}
     *
     * @param type Type
     */
    public HydraResponse(HydraResponseType type) {
        this(type, null);
    }

    /**
     * Get type
     *
     * @return {@link HydraResponseType}
     */
    public HydraResponseType getType() {
        return this.type;
    }

    /**
     * Set type
     *
     * @param type New type
     * @return {@link HydraResponse} instance
     */
    public HydraResponse withType(HydraResponseType type) {
        this.type = type;
        return this;
    }

    /**
     * Get message
     *
     * @return A message
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Set message
     *
     * @param message New message
     * @return {@link HydraResponse} instance
     */
    public HydraResponse withMessage(String message) {
        this.message = message;
        return this;
    }

}
