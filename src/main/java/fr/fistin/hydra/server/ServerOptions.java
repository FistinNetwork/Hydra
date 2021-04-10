package fr.fistin.hydra.server;

import java.util.ArrayList;
import java.util.List;

public class ServerOptions {

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

    public List<String> getEnv() {
        final List<String> env = new ArrayList<>();
        env.add("ALLOW_NETHER=" + this.nether);
        env.add("ANNOUNCE_PLAYER_ACHIEVEMENTS=" + this.broadcastAchievements);
        env.add("DIFFICULTY=" + this.difficulty);
        env.add("SPAWN_PROTECTION=" + this.spawnProtection);
        env.add("ALLOW_FLIGHT=" + this.flight);
        env.add("EULA=true");
        env.add("TYPE=SPIGOT");
        env.add("VERSION=1.8.8-R0.1-SNAPSHOT-latest");
        env.add("ONLINE_MODE=FALSE");
        return env;
    }
}