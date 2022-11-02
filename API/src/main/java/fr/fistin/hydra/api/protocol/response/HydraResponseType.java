package fr.fistin.hydra.api.protocol.response;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 24/11/2021 at 13:19
 */
public enum HydraResponseType {

    /** No response to send back */
    NONE,
    /** The request has been taken */
    OK,
    /** The request has been taken but an error occurred */
    NOT_OK,
    /** The request has been taken but need to be cancelled */
    ABORT

    ;

    /**
     * Create a response from a type
     *
     * @return {@link HydraResponse} object
     */
    public HydraResponse asResponse() {
        return new HydraResponse(this);
    }

}
