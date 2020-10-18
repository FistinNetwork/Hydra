package fr.fistinnetwork.hydra.servermanager;

import net.md_5.bungee.api.ChatColor;

public enum ServerState {
    CREATING(0,false),
    STARTING(1, false),
    READY(2, true),
    PLAYING(3, false),
    SHUTDOWN(4, false),
    IDLE(5, false);

    ServerState(int id, boolean access) {
        this.access = access;
        this.id = id;
    }
    private final boolean access;
    private final int id;

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        switch (this.id){
            case 0: // TODO Translation ?
                return this.toColor() + "Création";
            case 1:
                return this.toColor() + "Démarrage";
            case 2:
                return this.toColor() + "Prêt";
            case 3:
                return this.toColor() + "En jeu";
            case 4:
                return this.toColor() + "Arrêt";
            case 5:
                return this.toColor() + "Freeze";
            default:
                return this.toColor() + "?";
        }
    }

    public ChatColor toColor() {
        switch (this.id){
            case 0:
                return ChatColor.DARK_GRAY;
            case 1:
                return ChatColor.DARK_GREEN;
            case 2:
                return ChatColor.GREEN;
            case 3:
                return ChatColor.YELLOW;
            case 4:
                return ChatColor.RED;
            case 5:
                return ChatColor.DARK_RED;
            default:
                return ChatColor.WHITE;
        }
    }

    public boolean getAccess() {
        return this.access;
    }

    public static ServerState fromInteger(Integer id) {
        for (ServerState state : ServerState.values())
            if (state.getId() == id)
                return state;

        return null;
    }
}
