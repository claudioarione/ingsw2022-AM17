package it.polimi.ingsw.server;

import it.polimi.ingsw.model.Player;

import java.util.Locale;

public class PlayerClient {
    private final Communicable ch;
    private String username;
    private Player player;
    private Locale languageTag;

    public PlayerClient(Communicable ch, String username) {
        this.ch = ch;
        this.username = username;
        this.player = null;
    }

    public Communicable getCommunicable() {
        return ch;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Locale getLanguageTag() {
        return languageTag;
    }

    public void setLanguageTag(Locale languageTag) {
        this.languageTag = languageTag;
    }

}
