package eu.motogymkhana.competition.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import eu.motogymkhana.competition.model.Rider;

/**
 * Created by christine on 15-5-15.
 */
public class UpdateRiderRequest extends GymkhanaRequest {

    @JsonProperty("rider")
    private Rider rider;

    public UpdateRiderRequest(Rider rider) {
        this.rider = rider;
    }
}
