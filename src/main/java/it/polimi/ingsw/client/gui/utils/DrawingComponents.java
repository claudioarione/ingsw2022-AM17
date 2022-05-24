package it.polimi.ingsw.client.gui.utils;

import it.polimi.ingsw.model.game_objects.Color;
import it.polimi.ingsw.model.game_objects.Student;
import it.polimi.ingsw.server.game_state.CharacterState;
import it.polimi.ingsw.server.game_state.CloudState;
import it.polimi.ingsw.server.game_state.GameState;
import it.polimi.ingsw.server.game_state.PlayerState;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.List;

public class DrawingComponents {

    public static void drawFourPlayersGame(GameState gameState, double width, double height, AnchorPane root) {
        List<PlayerState> players = gameState.getPlayers();
        System.out.println("Width: " + width + " height: " + height);
        drawTwoDashboards(width, height, root, gameState);
        drawDashboard(players.get(2), 0, height - (width * (1454.0 / 3352.0) * 0.4), width * 0.4, root);
        drawDashboard(players.get(3), width * 0.6, height - (width * (1454.0 / 3352.0) * 0.4), width * 0.4, root);
    }

    public static void drawThreePlayersGame(GameState gameState, double width, double height, AnchorPane root) {
        List<PlayerState> players = gameState.getPlayers();
        drawTwoDashboards(width, height, root, gameState);
        drawDashboard(players.get(2), 0, height - (width * (1454.0 / 3352.0) * 0.4), width * 0.4, root);
    }

    public static void drawTwoPlayersGame(GameState gameState, double width, double height, AnchorPane root) {
        drawTwoDashboards(width, height, root, gameState);
    }

    private static void drawTwoDashboards(double width, double height, AnchorPane root, GameState gameState) {
        List<PlayerState> players = gameState.getPlayers();
        drawDashboard(players.get(0), 0, 0, width * 0.4, root);
        drawDashboard(players.get(1), width * 0.6, 0, width * 0.4, root);

        drawClouds(gameState.getClouds(), width, height, root);
        // Draw all three characters
        if (gameState.isExpert()) {
            drawCharacters(gameState.getCharacters(), width, height, root);
        }
    }

    private static void drawClouds(List<CloudState> clouds, double width, double height, AnchorPane root) {
        // TODO cloud's position is always the same, so you could show it directly in fxml file
    }

    private static void drawCharacters(List<CharacterState> characters, double width, double height, AnchorPane root) {
        double coordX = width * DrawingConstants.CHARACTERS_BEGINNING_PROPORTION;
        int heightProportion = 13;

        for (CharacterState character : characters) {
            String imagePath = "/gameboard/characters/" + character.getCharacterName() + ".jpg";
            System.out.println(imagePath);
            ImageView characterImage = new ImageView(new Image(imagePath));
            characterImage.setX(coordX);
            characterImage.setY(height / heightProportion);
            characterImage.setPreserveRatio(true);
            characterImage.setFitWidth(width * DrawingConstants.CHARACTER_CARD_PROPORTION);

            root.getChildren().add(characterImage);

            if (character.isHasCoin()) {
                ImageView coin = getImageView(
                        "/gameboard/Moneta_base.png",
                        coordX + width * (DrawingConstants.COIN_PROPORTION),
                        height / heightProportion,
                        width * (DrawingConstants.COIN_PROPORTION)
                );
                root.getChildren().add(coin);
            }
            if (character.getStudents() != null) {
                for (int i = 0; i < character.getStudents().size(); i++) {
                    String studentPath = "/gameboard/students/student_" +
                            character.getStudents().get(i).getColor().toString().toLowerCase() + ".png";
                    double fixedWidth = width / 25;
                    double x = coordX + ((i % 2 == 0) ? 0 : width * (DrawingConstants.CHARACTER_CARD_PROPORTION * 6 / 5));
                    double y = height / heightProportion + width * (DrawingConstants.CHARACTER_CARD_PROPORTION) + (i / 2) * fixedWidth;

                    root.getChildren().add(getImageView(studentPath, x, y, fixedWidth));
                }
            }

            coordX += width * (DrawingConstants.CHARACTER_CARD_PROPORTION + DrawingConstants.SPACE_BETWEEN_CHARACTERS_PROPORTION);
        }
    }

    private static void drawDashboard(PlayerState player, double x, double y, double width, AnchorPane root) {
        ImageView dashboard = new ImageView(new Image("/gameboard/Plancia_DEF_circles.png"));
        dashboard.setPreserveRatio(true);
        dashboard.setFitWidth(width);
        dashboard.setX(x);
        dashboard.setY(y);
        root.getChildren().add(dashboard);

        double coordX, coordY;
        // Add students to entrance
        for (int i = 0; i < player.getEntrance().size(); i++) {
            Student s = player.getEntrance().get(i);
            String resourceName = "/gameboard/students/student_" + s.getColor().toString().toLowerCase() + ".png";
            ImageView student = new ImageView(new Image(resourceName));
            student.setPreserveRatio(true);
            student.setFitWidth(width / 25);

            if (i % 2 == 0) {
                coordX = x + width / 10.7;
            } else {
                coordX = x + width / 29.412;
            }
            coordY = y + (width / 18.182) + (width / 13.793) * (i / 2 + i % 2);

            student.setX(coordX);
            student.setY(coordY);
            root.getChildren().add(student);
        }
        // Add students to dining room
        for (int i = 0; i < player.getDining().size(); i++) {
            Student s = player.getDining().get(i);
            String resourceName = "/gameboard/students/student_" + s.getColor().toString().toLowerCase() + ".png";
            ImageView student = new ImageView(new Image(resourceName));
            student.setPreserveRatio(true);
            student.setFitWidth(width / 25);

            coordY = y + width / 18.182;
            double deltaY = width / 13.793;
            switch (s.getColor()) {
                case RED -> coordY += deltaY;
                case YELLOW -> coordY += 2 * deltaY;
                case PINK -> coordY += 3 * deltaY;
                case BLUE -> coordY += 4 * deltaY;
            }

            int multiplyingNumber = 0;
            for (int j = 0; j < i; j++) {
                if (player.getDining().get(j).getColor() == s.getColor()) {
                    multiplyingNumber++;
                }
            }
            coordX = x + width / 5.33 + (width / 21.05) * multiplyingNumber;

            student.setX(coordX);
            student.setY(coordY);
            root.getChildren().add(student);
        }
        // Add professors
        for (Color color : player.getOwnedProfessors()) {
            String path = "/gameboard/professors/teacher_" + color.toString().toLowerCase() + ".png";
            ImageView professor = new ImageView(new Image(path));
            professor.setPreserveRatio(true);
            professor.setFitWidth(width / 20);
            professor.setRotate(90);

            coordX = x + width / 1.4184;
            coordY = y + width / 20;
            double deltaY = width / 14.035;
            switch (color) {
                case RED -> coordY += deltaY;
                case YELLOW -> coordY += 2 * deltaY;
                case PINK -> coordY += 3 * deltaY;
                case BLUE -> coordY += 4 * deltaY;
            }

            professor.setX(coordX);
            professor.setY(coordY);
            root.getChildren().add(professor);
        }
        // Add towers
        for (int i = 0; i < player.getRemainingTowers(); i++) {
            String path = "/gameboard/towers/" + player.getTowerColor().toString().toLowerCase() + "_tower.png";
            ImageView tower = new ImageView(new Image(path));
            tower.setPreserveRatio(true);
            tower.setFitWidth(width / 15.385);

            if (i % 2 == 0) {
                coordX = x + width / 1.242;
            } else {
                coordX = x + width / 1.133;
            }
            coordY = y + width / 13.333 + (i / 2) * (width / 13.793);

            tower.setX(coordX);
            tower.setY(coordY);
            root.getChildren().add(tower);
        }

    }

    private static ImageView getImageView(String path, double x, double y, double fixedWidth) {
        ImageView iv = new ImageView(new Image(path));
        iv.setX(x);
        iv.setY(y);
        iv.setPreserveRatio(true);
        iv.setFitWidth(fixedWidth);
        return iv;
    }
}
