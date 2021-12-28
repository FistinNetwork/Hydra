package fr.fistin.hydra.api.jwt;

import fr.fistin.hydra.api.HydraAPI;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 27/12/2021 at 17:58
 */
public class HydraJWTS {

    /** Claim name of the content */
    private static final String CONTENT_CLAIM = "content";

    /** {@link HydraAPI} instance */
    private final HydraAPI hydraAPI;

    /**
     * Constructor of {@link HydraJWTS}
     *
     * @param hydraAPI {@link HydraAPI} instance
     */
    public HydraJWTS(HydraAPI hydraAPI) {
        this.hydraAPI = hydraAPI;
    }

    /**
     * Transform a content to a jws<br>
     * Warning: this will work only if you provided a private key to the api
     *
     * @param content The content to transform
     * @return The jws
     */
    public String contentToJWS(String content) {
        final PrivateKey privateKey = this.hydraAPI.getPrivateKey();
        final Map<String, Object> claims = new HashMap<>();

        claims.put(CONTENT_CLAIM, content);

        return Jwts.builder()
                .addClaims(claims)
                .signWith(privateKey)
                .compact();
    }

    /**
     * Transform a jws to its content
     *
     * @param jws The jws to transform
     * @return The content of the jws
     */
    public String jwsToContent(String jws) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(this.hydraAPI.getPublicKey())
                    .build()
                    .parseClaimsJws(jws).getBody().get(CONTENT_CLAIM, String.class);
        } catch (JwtException e) {
            HydraAPI.log(Level.SEVERE, "Received an invalid JWS: " + jws + "! Error: " + e.getMessage());
            return null;
        }
    }

}
