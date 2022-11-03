package fr.fistin.hydra.api.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project: Hydra
 * Created by AstFaster
 * on 24/11/2021 at 18:33
 */
public class HydraServerOptions {

    private boolean pvp = true;
    private boolean nether = false;
    private boolean broadcastAchievements = false;
    private boolean flight = false;
    private String difficulty = "normal";
    private int spawnProtection = 0;

    public boolean isPvp() {
        return this.pvp;
    }

    public void setPvp(boolean pvp) {
        this.pvp = pvp;
    }

    public boolean isNether() {
        return this.nether;
    }

    public void setNether(boolean nether) {
        this.nether = nether;
    }

    public boolean isBroadcastAchievements() {
        return this.broadcastAchievements;
    }

    public void setBroadcastAchievements(boolean broadcastAchievements) {
        this.broadcastAchievements = broadcastAchievements;
    }

    public boolean isFlight() {
        return this.flight;
    }

    public void setFlight(boolean flight) {
        this.flight = flight;
    }

    public String getDifficulty() {
        return this.difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public int getSpawnProtection() {
        return this.spawnProtection;
    }

    public void setSpawnProtection(int spawnProtection) {
        this.spawnProtection = spawnProtection;
    }

    public Map<String, String> asEnv() {
        return new HashMap<String, String>() {{
            put("ALLOW_NETHER", String.valueOf(nether));
            put("ANNOUNCE_PLAYER_ACHIEVEMENTS", String.valueOf(broadcastAchievements));
            put("DIFFICULTY", difficulty);
            put("SPAWN_PROTECTION", String.valueOf(spawnProtection));
            put("ALLOW_FLIGHT", String.valueOf(flight));
        }};
    }

}
