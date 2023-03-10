package it.polimi.ingsw.model;

import it.polimi.ingsw.model.game_objects.Assistant;
import it.polimi.ingsw.model.game_objects.Color;
import it.polimi.ingsw.model.game_objects.TowerColor;
import it.polimi.ingsw.model.game_objects.Wizard;
import it.polimi.ingsw.model.game_objects.dashboard_objects.Dashboard;
import it.polimi.ingsw.model.game_objects.gameboard_objects.Island;
import it.polimi.ingsw.utils.constants.Constants;

import java.util.Arrays;
import java.util.List;

public class Player {
    private final Assistant[] hand;
    private final Dashboard dashboard;
    private final String name;
    private int numCoins;
    private Wizard wizard = null;
    private TowerColor towerColor = null;
    private Game game;
    private final int initialTowers;

    public Player(String name, int initialTowers) {
        this.name = name;
        this.initialTowers = initialTowers;
        this.numCoins = 1;
        this.dashboard = new Dashboard();
        this.hand = new Assistant[Constants.MAX_ASSISTANT_VALUE];
        for (int i = 1; i <= Constants.MAX_ASSISTANT_VALUE; i++) {
            hand[i - 1] = new Assistant(i % 2 != 0 ? i / 2 + 1 : i / 2, i, this);
        }
    }

    // For Game loading
    public Player(int[] assistants, Dashboard dashboard, String name, int numCoins, Wizard wizard, TowerColor towerColor, int initialTowers) {
        this.hand = new Assistant[10];
        for (int i = 1; i <= 10; i++) {
            final int check = i;
            if (Arrays.stream(assistants).anyMatch(a -> a == check)) {
                hand[i - 1] = new Assistant(i % 2 != 0 ? i / 2 + 1 : i / 2, i, this);
            } else {
                hand[i - 1] = null;
            }
        }
        this.dashboard = dashboard;
        this.name = name;
        this.numCoins = numCoins;
        this.wizard = wizard;
        this.towerColor = towerColor;
        this.initialTowers = initialTowers;
    }

    /**
     * Plays an {@code Assistant} from the hand of the {@code Player}
     *
     * @param assistant the {@code Assistant} to play
     */
    public void playAssistant(Assistant assistant) {
        hand[assistant.getValue() - 1] = null;
    }

    /**
     * Picks a {@code Wizard} not been picked by another {@code Player} yet
     *
     * @param wizard the {@code Wizard} chosen
     */
    public void pickWizard(Wizard wizard) {
        this.wizard = wizard;
    }

    /**
     * Add the {@code Player} to the given {@code Game}
     *
     * @param game the {@code Game} to add the {@code Player} to
     */
    public void addToGame(Game game) {
        this.game = game;
    }

    /**
     * Returns the remaining towers in the player dashboard
     *
     * @return the remaining towers in the player dashboard
     */
    public int getRemainingTowers() {
        int res = initialTowers;
        for (Island island : game.getGameBoard().getIslands()) {
            if (island.getTowerColor() == towerColor) {
                res -= island.getNumOfTowers();
            }
        }
        return res;
    }

    /**
     * Returns a {@code List} of the colors of the professors owned by the player
     *
     * @return a {@code List} of the colors of the professors owned by the player
     */
    public List<Color> getColorsOfOwnedProfessors() {
        return game.getGameBoard()
                .getColorsOfOwnedProfessors(this);
    }

    /**
     * Checks if the {@code Player} controls the professor of the given {@code Color}
     *
     * @param color the color of the professor to check
     * @return true if the {@code Player} controls the professor of the given {@code Color}
     */
    public boolean hasProfessor(Color color) {
        return game.getGameBoard()
                .getColorsOfOwnedProfessors(this)
                .contains(color);
    }

    public Assistant[] getHand() {
        return hand.clone();
    }

    public Dashboard getDashboard() {
        return dashboard;
    }

    public String getName() {
        return name;
    }

    public int getNumCoins() {
        return numCoins;
    }

    /**
     * Removes the selected amount of coins
     *
     * @param coins number of coins to be removed
     */
    public void removeCoins(int coins) {
        numCoins -= coins;
    }

    public void addCoin() {
        numCoins++;
    }

    public Wizard getWizard() {
        return wizard;
    }

    public TowerColor getTowerColor() {
        return towerColor;
    }

    public void setTowerColor(TowerColor towerColor) {
        this.towerColor = towerColor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        if (numCoins != player.numCoins) return false;
        if (initialTowers != player.initialTowers) return false;
        if (!Arrays.equals(hand, player.hand)) return false;
        if (!dashboard.equals(player.dashboard)) return false;
        if (!name.equals(player.name)) return false;
        if (wizard != player.wizard) return false;
        return towerColor == player.towerColor;
    }
}
