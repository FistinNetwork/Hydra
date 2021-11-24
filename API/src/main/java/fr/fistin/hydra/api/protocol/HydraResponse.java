package fr.fistin.hydra.api.protocol;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 24/11/2021 at 13:19
 */
public enum HydraResponse {

    /** The request has been taken */
    OK,

    /** The request has been taken but an error occurred */
    NOT_OK,

    /** The request has been taken but need to be cancelled */
    ABORT

}
