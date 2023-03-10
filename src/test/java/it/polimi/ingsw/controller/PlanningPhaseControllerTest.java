package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.GameEndedException;
import it.polimi.ingsw.languages.Messages;
import it.polimi.ingsw.messages.action.ServerActionMessage;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlanningPhaseControllerTest {

    Controller controller = new Controller();
    ClientHandlerStub rickCh = new ClientHandlerStub();
    ClientHandlerStub giuseCh = new ClientHandlerStub();
    ClientHandlerStub clodCh = new ClientHandlerStub();

    @BeforeAll
    static void initializeMessagesResourceBundle() {
        Messages.initializeBundle("en");
    }

    /**
     * Tests what happens when a player attempts to send an action message but the game is not started yet
     */
    @Test
    void testInvalidAction() throws GameEndedException {
        assertNull(controller.getGame());

        String playAssistantJson = "{status:ACTION,player:rick,action:{name:PLAY_ASSISTANT,args:{value:5}}}";
        controller.handleMessage(playAssistantJson, rickCh);

        ServerActionMessage response = ServerActionMessage.fromJson(rickCh.getJson());
        assertEquals("[ERROR] " + Messages.getMessage("game_not_started"), response.getDisplayText());
        assertEquals(1, response.getError());

        assertNull(controller.getGame());
    }

    /**
     * Tests what happens when the right player sends a message without "action" field or with a wrong action name
     */
    @Test
    void testMissingAction() throws GameEndedException {
        String noActionJson = "{status:ACTION,player:rick,typo:{name:PLAY_ASSISTANT,args:{value:5}}}";
        testInvalidRequest(noActionJson);

        String incorrectActionNameJson = "{status:ACTION,player:rick,typo:{name:PLAY,args:{value:5}}}";
        testInvalidRequest(incorrectActionNameJson);
    }

    /**
     * Tests what happens when an unknown user sends an action message pretending to be one of the players in the game
     */
    @Test
    void testMessageFromUnknownUser() throws GameEndedException {
        startNewExpertGameForTwo();

        String clodJson = "{status:ACTION,player:rick,action:{name:PLAY_ASSISTANT,args:{value:5}}}";
        controller.handleMessage(clodJson, clodCh);

        ServerActionMessage clodResponse = ServerActionMessage.fromJson(clodCh.getJson());
        assertEquals(3, clodResponse.getError());
        assertEquals("[ERROR] " + Messages.getMessage("not_logged_in"), clodResponse.getDisplayText());
    }

    /**
     * Tests what happens when the correct player sends a message with an invalid action name
     */
    @Test
    void testInvalidActionName() throws GameEndedException {
        startNewExpertGameForTwo();

        String rickJson = "{status:ACTION,player:rick,action:{name:TEST,args:{value:5}}}";
        controller.handleMessage(rickJson, rickCh);

        ServerActionMessage rickResponse = ServerActionMessage.fromJson(rickCh.getJson());
        assertEquals(3, rickResponse.getError());
        assertEquals("[ERROR] " + Messages.getMessage("invalid_request"), rickResponse.getDisplayText());
    }

    /**
     * Tests what happens when a player logged in sends a message with a username different from
     * the one he chose in the login phase
     */
    @Test
    void testInvalidUsernameAfterLogin() throws GameEndedException {
        startNewExpertGameForTwo();

        String invalidJson = "{status:ACTION,player:not_rick,action:{name:PLAY_ASSISTANT,args:{value:5}}}";
        controller.handleMessage(invalidJson, rickCh);

        ServerActionMessage response = ServerActionMessage.fromJson(rickCh.getJson());
        assertEquals("[ERROR] " + Messages.getMessage("invalid_identity"), response.getDisplayText());
        assertEquals(3, response.getError());
    }

    /**
     * Tests what happens when a player attempts to play an assistant when it's not his turn
     */
    @Test
    void testInvalidPlayAssistant1() throws GameEndedException {
        startNewExpertGameForTwo();

        String giuseInvalidJson = "{status:ACTION,player:giuse,action:{name:PLAY_ASSISTANT,args:{value:5}}}";
        controller.handleMessage(giuseInvalidJson, giuseCh);

        ServerActionMessage giuseResponse = ServerActionMessage.fromJson(giuseCh.getJson());
        assertEquals("[ERROR] " + Messages.getMessage("not_your_turn"), giuseResponse.getDisplayText());
        assertEquals(1, giuseResponse.getError());
    }

    /**
     * Tests what happens when a player attempts to play an assistant whose value is not valid
     */
    @Test
    void testInvalidPlayAssistant2() throws GameEndedException {
        startNewExpertGameForTwo();

        for (int value = 0; value < 12; value += 11) {
            String rickInvalidJson = "{status:ACTION,player:rick,action:{name:PLAY_ASSISTANT,args:{value:" + value + "}}}";
            controller.handleMessage(rickInvalidJson, rickCh);

            ServerActionMessage rickResponse = ServerActionMessage.fromJson(rickCh.getJson());
            assertEquals("[ERROR] " + Messages.getMessage("invalid_argument"), rickResponse.getDisplayText());
            assertEquals(2, rickResponse.getError());
        }
    }

    /**
     * Rick successfully plays assistant number 5
     */
    @Test
    void testValidPlayAssistant() throws GameEndedException {
        startNewExpertGameForTwo();

        String rickJson = "{status:ACTION,player:rick,action:{name:PLAY_ASSISTANT,args:{value:5}}}";
        controller.handleMessage(rickJson, rickCh);

        // Ensure Giuse is the next player
        assertEquals(controller.getLoggedUsers().get(1).getPlayer(),
                controller.getGame().getCurrentRound().getPlanningPhase().getNextPlayer());
    }

    /**
     * Tests what happens when Rick tries to play an assistant that he has already played or Giuse wants
     * to play an assistant of the same value of the one that Rick has played this planning phase
     */
    @Test
    void testAlreadyPlayedAssistant() throws GameEndedException {
        // Rick has played assistant number 5
        testValidPlayAssistant();

        String rickJson = "{status:ACTION,player:rick,action:{name:PLAY_ASSISTANT,args:{value:5}}}";
        controller.handleMessage(rickJson, rickCh);

        ServerActionMessage rickResponse = ServerActionMessage.fromJson(rickCh.getJson());
        assertEquals(2, rickResponse.getError());
        assertEquals("[ERROR] " + Messages.getMessage("already_played_assistant"), rickResponse.getDisplayText());

        String giuseJson = "{status:ACTION,player:giuse,action:{name:PLAY_ASSISTANT,args:{value:5}}}";
        controller.handleMessage(giuseJson, giuseCh);

        ServerActionMessage giuseResponse = ServerActionMessage.fromJson(giuseCh.getJson());
        assertEquals(2, giuseResponse.getError());
        assertEquals("[ERROR] " + Messages.getMessage("another_played_assistant"), giuseResponse.getDisplayText());
    }

    /**
     * Completes the first PlanningPhase in a two players match
     */
    @Test
    void testValidPlanningPhaseForTwo() throws GameEndedException {
        testValidPlayAssistant();
        assertNull(controller.getGame().getCurrentRound().getCurrentPlayerActionPhase());

        String giuseJson = "{status:ACTION,player:giuse,action:{name:PLAY_ASSISTANT,args:{value:4}}}";
        controller.handleMessage(giuseJson, giuseCh);

        // PlayerActionPhase for Giuse should now have been started
        assertNotNull(controller.getGame().getCurrentRound().getCurrentPlayerActionPhase());
        assertEquals(controller.getLoggedUsers().get(1).getPlayer(),
                controller.getGame().getCurrentRound().getCurrentPlayerActionPhase().getCurrentPlayer());

        ServerActionMessage giuseResponse = ServerActionMessage.fromJson(giuseCh.getJson());
        assertArrayEquals(new String[]{"MOVE_STUDENT_TO_DINING", "MOVE_STUDENT_TO_ISLAND", "PLAY_CHARACTER"},
                giuseResponse.getActions().toArray(new String[0]));
    }

    /**
     * Starts a new expert game - handling the login phase - for Rick and Giuse, that have to play their assistant in this order
     */
    public void startNewExpertGameForTwo() throws GameEndedException {
        String rickLoginJson = "{status:LOGIN,username:rick,action:SET_USERNAME,error:0}";
        String giuseLoginJson = "{status:LOGIN,username:giuse,action:SET_USERNAME,error:0}";

        controller.handleMessage(rickLoginJson, rickCh);
        controller.handleMessage(giuseLoginJson, giuseCh);

        String createGameJson = "{status:LOGIN,username:rick,action:CREATE_GAME,expert:true,numPlayers:2}";

        controller.handleMessage(createGameJson, rickCh);

        assertNotNull(controller.getGame());

        Player rick = controller.getLoggedUsers().get(0).getPlayer();
        Player giuse = controller.getLoggedUsers().get(1).getPlayer();
        // Rick has to play before Giuse
        controller.getGame().getCurrentRound().getPlanningPhase().setPlayersInOrder(new ArrayList<>(List.of(rick, giuse)));
    }

    /**
     * Sends the provided json message from Rick's Communicable and asserts that controller answers with
     * an INVALID_REQUEST displayText and an errorCode equal to 3
     *
     * @param jsonMessage the message to send from Rick's Communicable
     */
    private void testInvalidRequest(String jsonMessage) throws GameEndedException {
        startNewExpertGameForTwo();

        controller.handleMessage(jsonMessage, rickCh);

        ServerActionMessage response = ServerActionMessage.fromJson(rickCh.getJson());
        assertEquals("[ERROR] " + Messages.getMessage("invalid_request"), response.getDisplayText());
        assertEquals(3, response.getError());
    }

}