package it.polimi.ingsw.controller;

import it.polimi.ingsw.constants.Messages;
import it.polimi.ingsw.exceptions.GameEndedException;
import it.polimi.ingsw.messages.action.ServerActionMessage;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.game_objects.Color;
import it.polimi.ingsw.model.game_objects.Student;
import it.polimi.ingsw.model.game_objects.gameboard_objects.Island;
import it.polimi.ingsw.model.utils.Students;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ActionPhaseControllerTest {

    Controller controller = new Controller();
    ClientHandlerStub rickCh = new ClientHandlerStub();
    ClientHandlerStub giuseCh = new ClientHandlerStub();
    ClientHandlerStub clodCh = new ClientHandlerStub();
    Player rick, giuse;

    /**
     * In this test a player tries to play when it's not his turn
     */
    @Test
    void testInvalidTurn() throws GameEndedException {
        startActionPhase();

        String rickMoveStudent = "{status:ACTION,player:rick,action:{name:MOVE_STUDENT_TO_DINING,args:{color:GREEN}}}";
        controller.handleMessage(rickMoveStudent, rickCh);

        ServerActionMessage response = ServerActionMessage.fromJson(rickCh.getJson());
        assertEquals(1, response.getError());
        assertEquals("[ERROR] " + Messages.NOT_YOUR_TURN, response.getDisplayText());
    }

    /**
     * In this test a user who is logged in - but is waiting for his turn -
     * sends an action message pretending to be the player who has to play
     */
    @Test
    void testFakeAlias() throws GameEndedException {
        startActionPhase();

        // Field "player" has value "giuse" but the message comes from Rick's Communicable
        String json = "{status:ACTION,player:giuse,action:{name:MOVE_STUDENT_TO_DINING,args:{color:GREEN}}}";
        controller.handleMessage(json, rickCh);

        ServerActionMessage response = ServerActionMessage.fromJson(rickCh.getJson());
        assertEquals(3, response.getError());
        assertEquals("[ERROR] " + Messages.INVALID_IDENTITY, response.getDisplayText());
    }

    @Test
    void testMoveStudentToDining() throws GameEndedException {
        startActionPhase();
        fillEntrancesAndEmptyIslands();

        // Giuse does not own a green student
        String invalidJson = "{status:ACTION,player:giuse,action:{name:MOVE_STUDENT_TO_DINING,args:{color:GREEN}}}";
        controller.handleMessage(invalidJson, giuseCh);

        ServerActionMessage response = ServerActionMessage.fromJson(giuseCh.getJson());
        assertEquals(1, response.getError());
        assertEquals("[ERROR] " + Messages.ENTRANCE_DOESNT_CONTAIN_STUDENT, response.getDisplayText());

        for (int i = 1; i <= 3; i++) {
            String moveRedStudent = "{status:ACTION,player:giuse,action:{name:MOVE_STUDENT_TO_DINING,args:{color:RED}}}";
            controller.handleMessage(moveRedStudent, giuseCh);

            ServerActionMessage res = ServerActionMessage.fromJson(giuseCh.getJson());
            assertEquals(0, res.getError());
            assertEquals(i, Students.countColor(giuse.getDashboard().getDiningRoom().getStudents(), Color.RED));
            assertEquals(7 - i, Students.countColor(giuse.getDashboard().getEntrance().getStudents(), Color.RED));
        }

        assertEquals(2, giuse.getNumCoins());
        assertArrayEquals(new Color[]{Color.RED}, giuse.getColorsOfOwnedProfessors().toArray(new Color[0]));

    }

    @Test
    void testMotherNatureMove() throws GameEndedException {
        playUntilMotherNatureMove();

        String invalidMoveMnJson = "{status:ACTION,player:giuse,action:{name:MOVE_MN,args:{num_steps:4}}}";
        controller.handleMessage(invalidMoveMnJson, giuseCh);

        ServerActionMessage errorMessage = ServerActionMessage.fromJson(giuseCh.getJson());
        assertEquals(1, errorMessage.getError());
        assertEquals("[ERROR] " + Messages.INVALID_MN_MOVE, errorMessage.getDisplayText());

        String validMnMoveJson = "{status:ACTION,player:giuse,action:{name:MOVE_MN,args:{num_steps:1}}}";
        controller.handleMessage(validMnMoveJson, giuseCh);

        assertEquals(1, controller.getGame().getGameBoard().getMotherNatureIndex());
    }

    @Test
    void testChooseCloud() throws GameEndedException {
        playUntilMotherNatureMove();

        String invalidChooseCloudJson = "{status:ACTION,player:giuse,action:{name:FILL_FROM_CLOUD,args:{cloud:0}}}";
        controller.handleMessage(invalidChooseCloudJson, giuseCh);

        ServerActionMessage errorMessage = ServerActionMessage.fromJson(giuseCh.getJson());
        assertEquals(1, errorMessage.getError());
        assertEquals("[ERROR] " + Messages.MOVE_MN_FIRST, errorMessage.getDisplayText());
        assertFalse(controller.getGame().getGameBoard().getClouds().get(1).isEmpty());

        String validMnMoveJson = "{status:ACTION,player:giuse,action:{name:MOVE_MN,args:{num_steps:1}}}";
        controller.handleMessage(validMnMoveJson, giuseCh);

        String invalidCloudJson = "{status:ACTION,player:giuse,action:{name:FILL_FROM_CLOUD,args:{cloud:2}}}";
        controller.handleMessage(invalidCloudJson, giuseCh);

        ServerActionMessage invalidCloudMessage = ServerActionMessage.fromJson(giuseCh.getJson());
        assertEquals(1, invalidCloudMessage.getError());
        assertEquals("[ERROR] " + Messages.INVALID_CLOUD, invalidCloudMessage.getDisplayText());
        assertFalse(controller.getGame().getGameBoard().getClouds().get(1).isEmpty());

        String validCloudJson = "{status:ACTION,player:giuse,action:{name:FILL_FROM_CLOUD,args:{cloud:1}}}";
        controller.handleMessage(validCloudJson, giuseCh);

        assertTrue(controller.getGame().getGameBoard().getClouds().get(1).isEmpty());
        assertFalse(controller.getGame().getGameBoard().getClouds().get(0).isEmpty());

        // It's Rick's turn
        assertEquals(rick, controller.getGame().getCurrentRound().getCurrentPlayerActionPhase().getCurrentPlayer());
    }

    @Test
    void testPlayCharacter() throws GameEndedException {
        playUntilMotherNatureMove();


    }

    /**
     * Handles the login and the planning phase of a new expert game for two, during which
     * Rick plays assistant number 5 and Giuse assistant number 4.
     * That means Giuse has to play his PlayerActionPhase
     */
    private void startActionPhase() throws GameEndedException {
        String rickLoginJson = "{status:LOGIN,username:rick,action:SET_USERNAME,error:0}";
        String giuseLoginJson = "{status:LOGIN,username:giuse,action:SET_USERNAME,error:0}";

        controller.handleMessage(rickLoginJson, rickCh);
        controller.handleMessage(giuseLoginJson, giuseCh);

        String createGameJson = "{status:LOGIN,username:rick,action:CREATE_GAME,expert:true,numPlayers:2}";

        controller.handleMessage(createGameJson, rickCh);

        rick = controller.getLoggedUsers().get(0).getPlayer();
        giuse = controller.getLoggedUsers().get(1).getPlayer();
        // Rick has to play before Giuse
        controller.getGame().getCurrentRound().getPlanningPhase().setPlayersInOrder(new ArrayList<>(List.of(rick, giuse)));

        String rickPlayAssistantJson = "{status:ACTION,player:rick,action:{name:PLAY_ASSISTANT,args:{value:5}}}";
        String giusePlayAssistantJson = "{status:ACTION,player:giuse,action:{name:PLAY_ASSISTANT,args:{value:4}}}";

        controller.handleMessage(rickPlayAssistantJson, rickCh);
        controller.handleMessage(giusePlayAssistantJson, giuseCh);

        rick.getDashboard().getEntrance().setStudents(new ArrayList<>());
    }

    /**
     * Rick has 7 PINK students in his entrance
     * Giuse has 7 RED students in his entrance
     * All islands are empty and mother nature is in the first island
     */
    private void fillEntrancesAndEmptyIslands() {
        ArrayList<Student> rickStudents = new ArrayList<>();
        ArrayList<Student> giuseStudents = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            rickStudents.add(new Student(Color.PINK));
            giuseStudents.add(new Student(Color.RED));
        }
        rick.getDashboard().getEntrance().setStudents(rickStudents);
        giuse.getDashboard().getEntrance().setStudents(giuseStudents);

        for (Island island : controller.getGame().getGameBoard().getIslands()) {
            island.setStudents(new ArrayList<>());
        }

        controller.getGame().getGameBoard().setMotherNatureIndex(0);
    }

    /**
     * Calls the two methods startActionPhase() and fillEntrancesAndEmptyIslands() and moves three red
     * students from Giuse's entrance to his dining room
     */
    private void playUntilMotherNatureMove() throws GameEndedException {
        startActionPhase();
        fillEntrancesAndEmptyIslands();
        for (int i = 0; i < 3; i++) {
            String moveRedStudent = "{status:ACTION,player:giuse,action:{name:MOVE_STUDENT_TO_DINING,args:{color:RED}}}";
            controller.handleMessage(moveRedStudent, giuseCh);
        }

    }

}