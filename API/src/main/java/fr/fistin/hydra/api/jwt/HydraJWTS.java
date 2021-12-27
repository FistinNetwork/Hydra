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

    private static final String CONTENT_CLAIM = "content";

    private final HydraAPI hydraAPI;

    public HydraJWTS(HydraAPI hydraAPI) {
        this.hydraAPI = hydraAPI;
    }

    public String contentToJWS(String content) {
        final PrivateKey privateKey = this.hydraAPI.getPrivateKey();
        final Map<String, Object> claims = new HashMap<>();

        claims.put(CONTENT_CLAIM, content);

        return Jwts.builder()
                .addClaims(claims)
                .signWith(privateKey)
                .compact();
    }

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
