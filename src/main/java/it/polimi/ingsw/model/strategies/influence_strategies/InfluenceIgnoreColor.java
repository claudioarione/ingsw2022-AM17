package it.polimi.ingsw.model.strategies.influence_strategies;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.game_objects.Color;
import it.polimi.ingsw.model.game_objects.gameboard_objects.Island;
import it.polimi.ingsw.model.utils.Students;

import java.util.ArrayList;

public class InfluenceIgnoreColor implements InfluenceStrategy {

    private final Color colorToIgnore;

    public InfluenceIgnoreColor(Color colorToIgnore) {
        this.colorToIgnore = colorToIgnore;
    }

    /**
     * The strategy to calculate the influence of the selected {@code Island} when the effect of the
     * {@code Character} called "ignoreColor" is active
     *
     * @param island the {@code Island} to be considered
     * @param team   the {@code Player} to calculate the influence of
     * @return an int representing the influence of the player on the island
     */
    @Override
    public int computeInfluence(Island island, ArrayList<Player> team) {
        int difference = 0;
        for (Player player : team) {
            if (player.hasProfessor(colorToIgnore)) {
                difference = Students.countColor(island.getStudents(), colorToIgnore);
            }
        }

        return InfluenceDefault.computeDefaultInfluence(island, team) - difference;
    }

}
