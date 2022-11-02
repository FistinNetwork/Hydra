package fr.fistin.hydra.api.server;

import fr.fistin.hydra.api.HydraAPI;
import fr.fistin.hydra.api.HydraException;
import fr.fistin.hydra.api.protocol.HydraChannel;
import fr.fistin.hydra.api.protocol.response.HydraResponseCallback;
import fr.fistin.hydra.api.protocol.response.HydraResponseType;
import fr.fistin.hydra.api.server.packet.HydraStartServerPacket;
import fr.fistin.hydra.api.server.packet.HydraStopServerPacket;
import fr.fistin.hydra.api.server.packet.HydraUpdateServerPacket;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by AstFaster
 * on 01/11/2022 at 19:56
 */
public class HydraServersService {

    /** The servers Redis hash */
    public static final String HASH = HydraAPI.HYDRA_HASH + "servers:";

    /** The {@link HydraAPI} instance */
    private final HydraAPI hydraAPI;

    /**
     * Constructor of a {@link HydraServersService}
     *
     * @param hydraAPI The {@link HydraAPI} instance
     */
    public HydraServersService(HydraAPI hydraAPI) {
        this.hydraAPI = hydraAPI;
    }

    /**
     * Create a server with given information by querying Hydra.
     *
     * @param serverInfo The information of the server to create
     */
    public void createServer(HydraServerCreationInfo serverInfo, Consumer<HydraServer> onCreated) {
        if (serverInfo.getType() == null || serverInfo.getData() == null || serverInfo.getOptions() == null || serverInfo.getAccessibility() == null || serverInfo.getProcess() == null) {
            throw new HydraException("Invalid server creation information!");
        }

        this.hydraAPI.getConnection().sendPacket(HydraChannel.QUERY, new HydraStartServerPacket(serverInfo))
                .withResponseCallback(response -> {
                    final HydraResponseType type = response.getType();

                    if (type == HydraResponseType.OK) {
                        final HydraServer createdServer = response.getMessage(HydraServer.class);

                        onCreated.accept(createdServer);
                    } else {
                        final String message = response.getMessage();

                        throw new HydraException("Couldn't create server! Return response: " + type + (message != null ? "(message: " + message + ")" : ""));
                    }
                })
                .exec();
    }

    /**
     * Update a server in cache by asking Hydra.<br>
     * Only the concerned server can perform this action.
     *
     * @param server The server to update
     */
    public void updateServer(HydraServer server) {
        if (!this.hydraAPI.getApplication().equals(server.getName())) {
            throw new HydraException("Servers can only be updated by themself or Hydra!");
        }

        this.hydraAPI.getConnection().sendPacket(HydraChannel.SERVERS, new HydraUpdateServerPacket(server)).exec();
    }

    /**
     * Stop a running server by querying Hydra
     *
     * @param name The name of the server to stop
     * @param callback The callback to trigger after the server has been stopped
     */
    public void stopServer(String name, HydraResponseCallback callback) {
        this.hydraAPI.getConnection().sendPacket(HydraChannel.QUERY, new HydraStopServerPacket(name))
                .withResponseCallback(callback)
                .exec();
    }

    /**
     * Stop a running server by querying Hydra
     *
     * @param name The name of the server to stop
     */
    public void stopServer(String name) {
        this.stopServer(name, null);
    }

    /**
     * Get a server from the Redis cache.
     *
     * @param name The name of the server to get
     * @return The found {@link HydraServer}; or <code>null</code> if nothing was found
     */
    public HydraServer getServer(String name) {
        return this.hydraAPI.getRedis().get(jedis -> {
            final String json = jedis.get(HASH + name);

            return json == null ? null : HydraAPI.GSON.fromJson(json, HydraServer.class);
        });
    }

    /**
     * Get all servers from Redis cache.
     *
     * @return A list of {@link HydraServer}
     */
    public List<HydraServer> getServers() {
        return this.hydraAPI.getRedis().get(jedis -> {
            final List<HydraServer> servers = new ArrayList<>();

            for (String key : jedis.keys(HASH + "*")) {
                servers.add(HydraAPI.GSON.fromJson(jedis.get(key), HydraServer.class));
            }
            return servers;
        });
    }

    /**
     * Get all servers with the same type from Redis cache.
     *
     * @param type The type of the servers to get
     * @return A list of {@link HydraServer} with the same type
     */
    public List<HydraServer> getServers(String type) {
        return this.hydraAPI.getRedis().get(jedis -> {
            final List<HydraServer> servers = new ArrayList<>();

            for (String key : jedis.keys(HASH + type + "-*")) {
                servers.add(HydraAPI.GSON.fromJson(jedis.get(key), HydraServer.class));
            }
            return servers;
        });
    }

}
