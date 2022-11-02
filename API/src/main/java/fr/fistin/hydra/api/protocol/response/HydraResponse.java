package fr.fistin.hydra.api.protocol.response;

import fr.fistin.hydra.api.HydraAPI;

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
     * Constructor of  a {@link HydraResponse} object
     *
     * @param type Type
     */
    public HydraResponse(HydraResponseType type) {
        this(type, null);
    }

    /**
     * Get the type of the response
     *
     * @return {@link HydraResponseType}
     */
    public HydraResponseType getType() {
        return this.type;
    }

    /**
     * Set the type of the response
     *
     * @param type New type
     * @return {@link HydraResponse} instance
     */
    public HydraResponse withType(HydraResponseType type) {
        this.type = type;
        return this;
    }

    /**
     * Get the response message
     *
     * @return A message
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Get the response message as an object.<br>
     * The message will be deserialized from JSON format.
     *
     * @param outputClass The class of the output object
     * @return The message as an object
     * @param <T> The type of the object to return
     */
    public <T> T getMessage(Class<T> outputClass) {
        return HydraAPI.GSON.fromJson(this.message, outputClass);
    }

    /**
     * Set response message as {@link String}
     *
     * @param message New message
     * @return This {@link HydraResponse} instance
     */
    public HydraResponse withMessage(String message) {
        this.message = message;
        return this;
    }

    /**
     * Set message as an object that will be serialized in JSON format.
     *
     * @param message New message
     * @return This {@link HydraResponse} instance
     */
    public HydraResponse withMessage(Object message) {
        this.message = HydraAPI.GSON.toJson(message);
        return this;
    }

}
