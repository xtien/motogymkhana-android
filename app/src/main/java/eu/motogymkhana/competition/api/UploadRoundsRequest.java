package eu.motogymkhana.competition.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;

import eu.motogymkhana.competition.model.Round;

/**
 * Created by christine on 27-5-15.
 */
public class UploadRoundsRequest extends GymkhanaRequest {

    @JsonProperty("rounds")
    private Collection<Round> rounds;

    public UploadRoundsRequest(Collection<Round> rounds) {
        this.rounds = rounds;
    }
}
