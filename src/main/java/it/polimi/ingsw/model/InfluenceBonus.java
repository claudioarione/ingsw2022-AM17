package it.polimi.ingsw.model;

public class InfluenceBonus implements InfluenceStrategy {
    private final Player buffedPlayer;
    private final int bonus;

    public InfluenceBonus(Player buffedPlayer, int bonus) {
        this.buffedPlayer = buffedPlayer;
        this.bonus = bonus;
    }

    public InfluenceBonus(Player buffedPlayer) {
        this.buffedPlayer = buffedPlayer;
        this.bonus = 2;
    }


    @Override
    public int computeInfluence(Island island, Player player) {
        return InfluenceDefault.computeDefaultInfluence(island, player) + bonus;
    }
}
