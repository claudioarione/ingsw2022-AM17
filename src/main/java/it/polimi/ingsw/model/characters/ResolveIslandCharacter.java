package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.game_actions.PlayerActionPhase;
import it.polimi.ingsw.model.game_objects.Color;
import it.polimi.ingsw.model.game_objects.gameboard_objects.GameBoard;
import it.polimi.ingsw.model.game_objects.gameboard_objects.Island;

import java.util.List;

public class ResolveIslandCharacter extends GameboardCharacter {

    public ResolveIslandCharacter(CharacterName characterName, GameBoard gb) {
        super(characterName, gb);
    }

    public ResolveIslandCharacter(CharacterName characterName, GameBoard gb, boolean hasCoin) {
        super(characterName, gb, hasCoin);
    }

    /**
     * Resolves the selected {@code Island} as if MotherNature stopped on it
     *
     * @param currentPlayerActionPhase the {@code PlayerActionPhase} which the effect is used in
     * @param island                   the {@code Island} which the {@code Character} affects
     * @param color                    the {@code Color} which the {@code Character} affects
     * @param srcColors                the students to be moved to the destination
     * @param dstColors                the students to be moved to the source (only if the effect is a "swap" effect)
     */
    @Override
    public void useEffect(PlayerActionPhase currentPlayerActionPhase, Island island, Color color, List<Color> srcColors, List<Color> dstColors) {
        currentPlayerActionPhase.resolveIsland(island);
    }
}
