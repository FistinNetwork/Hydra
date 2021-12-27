package fr.fistin.hydra.api.jwt;

import fr.fistin.hydra.api.HydraAPI;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import java.security.PrivateKey;
import java.util.logging.Level;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 27/12/2021 at 17:58
 */
public class HydraJWTManager {

    private final HydraAPI hydraAPI;

    public HydraJWTManager(HydraAPI hydraAPI) {
        this.hydraAPI = hydraAPI;
    }

    public String toJWT(String message) {
        final PrivateKey privateKey = this.hydraAPI.getPrivateKey();

        if (this.hydraAPI.getType() == HydraAPI.Type.SERVER && privateKey != null) {
            return Jwts.builder()
                    .setPayload(new HydraJWTPayload(message).toJson())
                    .signWith(privateKey)
                    .compact();
        }
        return message;
    }

    public String fromJWT(String jwt) {
        try {
            final Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(this.hydraAPI.getPublicKey())
                    .build()
                    .parseClaimsJws(jwt);

            return HydraJWTPayload.payloadToContent(claims.getBody());
        } catch (JwtException e) {
            return jwt;
        }
    }

}
