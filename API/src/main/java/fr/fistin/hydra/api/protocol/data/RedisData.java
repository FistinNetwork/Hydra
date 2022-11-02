package fr.fistin.hydra.api.protocol.data;

/**
 * Created by AstFaster
 * on 01/11/2022 at 14:19
 */
public class RedisData {

    /** The hostname of Redis database */
    private final String hostname;
    /** The port Redis database is running at */
    private final int port;
    /** The password asked by the Redis database to connect */
    private final String password;

    /**
     * Create a {@link RedisData} object
     *
     * @param hostname The hostname of the Redis database
     * @param port The port of the Redis database
     * @param password The password of the Redis database
     */
    public RedisData(String hostname, int port, String password) {
        this.hostname = hostname;
        this.port = port;
        this.password = password;
    }

    /**
     * Get the hostname of the Redis database
     *
     * @return A hostname
     */
    public String getHostname() {
        return this.hostname;
    }

    /**
     * Get the port of the Redis database
     *
     * @return A port
     */
    public int getPort() {
        return this.port;
    }

    /**
     * Get the password of the Redis database
     *
     * @return A password
     */
    public String getPassword() {
        return this.password;
    }

}
