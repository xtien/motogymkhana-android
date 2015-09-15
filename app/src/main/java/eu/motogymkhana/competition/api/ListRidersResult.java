package eu.motogymkhana.competition.api;

import java.util.Collection;

import eu.motogymkhana.competition.model.Rider;

public class ListRidersResult extends GymkhanaResult {

    public static final int OK = 0;

    private int result;
    private Collection<Rider> riders;
    private String text;

    public Collection<Rider> getRiders() {
        return riders;
    }

    public void setRiders(Collection<Rider> riders) {
        this.riders = riders;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
