package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.messages.login.GameLobby;
import it.polimi.ingsw.server.game_state.GameState;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import java.util.List;

public class GameParametersController implements GuiController {

    private final ObservableList<Integer> numPlayersChoices = FXCollections.observableArrayList(2, 3, 4);
    @FXML
    private ChoiceBox<Integer> numPlayersChoiceBox;
    @FXML
    private CheckBox isGameExpert;
    @FXML
    private Label title;

    @FXML
    void initialize() {
        title.setText("Welcome, " + GuiView.getGui().getUsername());

        numPlayersChoiceBox.setValue(2);
        numPlayersChoiceBox.setItems(numPlayersChoices);

        isGameExpert.setAllowIndeterminate(false);
    }

    public GameParametersController() {
    }

    public void onStartButtonClicked(ActionEvent event) {
        GuiView.getGui().getCurrentObserver().sendLoginParameters(null, numPlayersChoiceBox.getValue(), isGameExpert.isSelected());
    }


    @Override
    public void receiveData(GameLobby lobby, GameState gameState, List<String> actions, String message) {

    }
}
