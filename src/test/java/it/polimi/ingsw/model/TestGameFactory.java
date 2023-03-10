package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.InvalidActionException;
import it.polimi.ingsw.exceptions.InvalidStudentException;
import it.polimi.ingsw.languages.Messages;
import it.polimi.ingsw.model.game_objects.Color;
import it.polimi.ingsw.model.game_objects.Student;
import it.polimi.ingsw.model.game_objects.dashboard_objects.Entrance;

import java.util.ArrayList;
import java.util.List;

public class TestGameFactory {

    /**
     * Creates an expert {@code Game} for three players, whose nicknames are Rick, Clod and Giuse
     * All the entrances of the players are empty
     *
     * @return the created {@code Game}
     */
    public static Game getNewGame() {
        Messages.initializeBundle("en");
        ArrayList<Player> players = createThreePlayers();
        Game res = new Game(players, true);
        res.start(0);
        for (Player player : players) {
            Entrance entrance = player.getDashboard().getEntrance();
            for (int i = 0; i < 9; i++) {
                try {
                    entrance.giveStudent(res.getGameBoard().getBag(), entrance.getStudents().get(0));
                } catch (InvalidStudentException | InvalidActionException e) {
                    e.printStackTrace();
                }
            }
        }
        return res;
    }

    /**
     * Creates an expert 4-player {@code Game}
     *
     * @return the created {@code Game}
     */
    public static Game getNewFourPlayersGame() {
        Game res = new Game(createFourPlayers(), true);
        res.start(0);
        return res;
    }

    /**
     * A helper method to add two players to the {@code Game}
     *
     * @return the {@code ArrayList} of players added
     */
    public static ArrayList<Player> createTwoPlayers() {
        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player("Rick", 8));
        players.add(new Player("Clod", 8));
        return players;
    }

    /**
     * A helper method add three players to the {@code Game}
     *
     * @return the {@code ArrayList} of players added
     */
    private static ArrayList<Player> createThreePlayers() {
        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player("Rick", 6));
        players.add(new Player("Clod", 6));
        players.add(new Player("Giuse", 6));
        return players;
    }

    /**
     * A helper method add four players to the {@code Game}
     *
     * @return the {@code ArrayList} of players added
     */
    private static ArrayList<Player> createFourPlayers() {
        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player("Rick", 8));
        players.add(new Player("Clod", 8));
        players.add(new Player("Giuse", 8));
        players.add(new Player("Fabio", 8));
        return players;
    }

    public static void fillThreeEntrances(Player p1, Player p2, Player p3) {
        // Can't take students from bag because it adds randomness
        for (int i = 0; i < 5; i++) {
            p2.getDashboard().getEntrance().receiveStudent(new Student(Color.GREEN));
            p1.getDashboard().getEntrance().receiveStudent(new Student(Color.PINK));
            p3.getDashboard().getEntrance().receiveStudent(new Student(Color.BLUE));
        }
        for (int i = 0; i < 4; i++) {
            p1.getDashboard().getEntrance().receiveStudent(new Student(Color.GREEN));
            p3.getDashboard().getEntrance().receiveStudent(new Student(Color.PINK));
            p2.getDashboard().getEntrance().receiveStudent(new Student(Color.BLUE));
        }
    }

    /**
     * Utility function that converts a list of student to a list of the corresponding colors
     *
     * @param students a {@code List} of students
     * @return the corresponding {@code List} of colors
     */
    public static List<Color> fromListOfStudentToListOfColor(List<Student> students) {
        List<Color> colors = new ArrayList<>();
        for (Student student : students) {
            colors.add(student.getColor());
        }
        return colors;
    }
}
