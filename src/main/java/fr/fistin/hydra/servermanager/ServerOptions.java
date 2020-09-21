package fr.fistin.hydra.servermanager;

import java.util.ArrayList;
import java.util.List;

public class ServerOptions {
    private boolean pvp = true;
    private boolean nether = false;
    private boolean broadcastAchivements = false;
    private boolean flight = false;
    private String difficulty = "normal";
    private int spawnProtection = 0;

    public boolean isPvp() {
        return pvp;
    }

    public void setPvp(boolean pvp) {
        this.pvp = pvp;
    }

    public boolean isNether() {
        return nether;
    }

    public void setNether(boolean nether) {
        this.nether = nether;
    }

    public boolean isBroadcastAchivements() {
        return broadcastAchivements;
    }

    public void setBroadcastAchivements(boolean broadcastAchivements) {
        this.broadcastAchivements = broadcastAchivements;
    }

    public boolean isFlight() {
        return flight;
    }

    public void setFlight(boolean flight) {
        this.flight = flight;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public int getSpawnProtection() {
        return spawnProtection;
    }

    public void setSpawnProtection(int spawnProtection) {
        this.spawnProtection = spawnProtection;
    }

    public List<String> getEnv() {
        List<String> result = new ArrayList<String>();
        result.add("PVP=" + this.pvp);
        result.add("ALLOW_NETHER=" + this.nether);
        result.add("ANNOUNCE_PLAYER_ACHIEVEMENTS=" + this.broadcastAchivements);
        result.add("DIFFICULTY=" + this.difficulty);
        result.add("SPAWN_PROTECTION=" + this.spawnProtection);
        result.add("ALLOW_FLIGHT=" + this.flight);
        result.add("EULA=true");
        result.add("TYPE=SPIGOT");
        result.add("VERSION=1.8.8-R0.1-SNAPSHOT-latest");
        result.add("ONLINE_MODE=FALSE");
        return result;
    }

    public ServerOptions() { }
}
