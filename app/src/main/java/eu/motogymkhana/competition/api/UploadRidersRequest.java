package eu.motogymkhana.competition.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import eu.motogymkhana.competition.model.Rider;

/**
 * Created by christine on 13-5-15.
 */
public class UploadRidersRequest extends GymkhanaRequest {

    @JsonProperty("riders")
    private List<Rider> riders;

    public UploadRidersRequest(List<Rider> riders) {
        this.riders = riders;
    }
}
