package fr.fistin.hydra.api.protocol.environment;

import io.jsonwebtoken.SignatureAlgorithm;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 28/12/2021 at 20:51
 */
public class HydraEnvironment {

    /** The Redis host address */
    private final String redisHost;
    /** The port of Redis */
    private final short redisPort;
    /** The password to connect to Redis */
    private final String redisPassword;
    /** The client public key */
    private final PublicKey publicKey;

    /**
     * Constructor of {@link HydraEnvironment}
     *
     * @param redisHost The Redis host
     * @param redisPort The Redis port
     * @param redisPassword The Redis password
     * @param publicKey The public key
     */
    public HydraEnvironment(String redisHost, short redisPort, String redisPassword, PublicKey publicKey) {
        this.redisHost = redisHost;
        this.redisPort = redisPort;
        this.redisPassword = redisPassword;
        this.publicKey = publicKey;
    }

    /**
     * Get the Redis host address
     *
     * @return Redis host
     */
    public String getRedisHost() {
        return this.redisHost;
    }

    /**
     * Get the Redis port
     *
     * @return Redis port
     */
    public short getRedisPort() {
        return this.redisPort;
    }

    /**
     * Get the Redis password
     *
     * @return Redis password
     */
    public String getRedisPassword() {
        return this.redisPassword;
    }

    /**
     * Get the public key
     *
     * @return The {@link PublicKey}
     */
    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    /**
     * Get the environment in a list format
     *
     * @return A list of environment variables
     */
    public List<String> get() {
        final List<String> envs = new ArrayList<>();

        envs.add("REDIS_HOST=" + this.redisHost);
        envs.add("REDIS_PORT=" + String.valueOf(this.redisPort));
        envs.add("REDIS_PASS=" + this.redisPassword);
        envs.add("PUBLIC_KEY=" + Base64.getEncoder().encodeToString(this.publicKey.getEncoded()));

        return envs;
    }

    /**
     * Load all the environment variables<br>
     * Warning: It will work only if the application is a {@link fr.fistin.hydra.api.HydraAPI.Type#CLIENT}
     *
     * @param logger The logger used to print some info
     * @return {@link HydraEnvironment} object
     */
    public static HydraEnvironment load(Logger logger) {
        logger.log(Level.INFO, "Loading environment variables...");

        final String redisHost = System.getenv("REDIS_HOST");
        final short redisPort = Short.parseShort(System.getenv("REDIS_PORT"));
        final String redisPassword = System.getenv("REDIS_PASS");
        final PublicKey publicKey = readPublicKey(logger);

        return new HydraEnvironment(redisHost, redisPort, redisPassword, publicKey);
    }

    /**
     * Read the public key from environment variables
     *
     * @param logger The logger used to print some info
     * @return {@link PublicKey} used to verify received messages
     */
    private static PublicKey readPublicKey(Logger logger) {
        logger.log(Level.INFO, "Reading public key...");

        try {
            return KeyFactory.getInstance(SignatureAlgorithm.RS256.getFamilyName()).generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(System.getenv("PUBLIC_KEY"))));
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            return null;
        }
    }

}
