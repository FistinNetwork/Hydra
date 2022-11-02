package fr.fistin.hydra.api.protocol.heartbeat;

import fr.fistin.hydra.api.HydraAPI;
import fr.fistin.hydra.api.protocol.HydraChannel;

import java.util.concurrent.TimeUnit;

/**
 * Created by AstFaster
 * on 02/11/2022 at 11:33
 */
public class HydraHeartbeatTask {

    /** The {@link HydraAPI} instance */
    private final HydraAPI hydraAPI;

    /**
     * Constructor of a {@link HydraHeartbeatTask}
     *
     * @param hydraAPI The {@link HydraAPI} instance
     */
    public HydraHeartbeatTask(HydraAPI hydraAPI) {
        this.hydraAPI = hydraAPI;

        if (this.hydraAPI.getType() == HydraAPI.Type.SERVER || this.hydraAPI.getType() == HydraAPI.Type.PROXY) {
            this.hydraAPI.getExecutorService().scheduleAtFixedRate(this::heartbeat, 0, 10, TimeUnit.SECONDS);
        }
    }

    /**
     * Send the heartbeat to Hydra
     */
    private void heartbeat() {
        this.hydraAPI.getConnection().sendPacket(this.hydraAPI.getType() == HydraAPI.Type.SERVER ? HydraChannel.SERVERS : HydraChannel.PROXIES, new HydraHeartbeatPacket()).exec();
    }

}
