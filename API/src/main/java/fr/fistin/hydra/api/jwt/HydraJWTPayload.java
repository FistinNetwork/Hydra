package fr.fistin.hydra.api.jwt;

import fr.fistin.hydra.api.HydraAPI;
import io.jsonwebtoken.Claims;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 27/12/2021 at 17:59
 */
public class HydraJWTPayload {

    private String content;

    public HydraJWTPayload(String content) {
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String toJson() {
        return HydraAPI.GSON.toJson(this);
    }

    public static String payloadToContent(Claims payload) {
        return (String) payload.get("content");
    }

}
