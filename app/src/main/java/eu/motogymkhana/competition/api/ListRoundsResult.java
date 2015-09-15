package eu.motogymkhana.competition.api;

import java.util.Collection;

import eu.motogymkhana.competition.model.Round;

public class ListRoundsResult extends GymkhanaResult {

    public static final int OK = 0;

    private int result;
    private Collection<Round> rounds;

    public Collection<Round> getRounds() {
        return rounds;
    }

    public void setRounds(Collection<Round> rounds) {
        this.rounds = rounds;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
