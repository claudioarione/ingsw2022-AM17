package it.polimi.ingsw.messages.update;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.server.game_state.GameState;
import it.polimi.ingsw.utils.constants.Constants;

public class UpdateMessage extends Message {
    private GameState gameState;
    private String displayText;

    public UpdateMessage() {
        super();
        setStatus(Constants.STATUS_UPDATE);
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public String getDisplayText() {
        return displayText;
    }

    public void setDisplayText(String displayText) {
        this.displayText = displayText;
    }

    /**
     * Returns an {@code UpdateMessage} object from a Json {@code String}
     *
     * @param json the Json {@code String}
     * @return an {@code UpdateMessage} object from a Json {@code String}
     */
    public static UpdateMessage fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, new TypeToken<UpdateMessage>() {
        }.getType());
    }
}
