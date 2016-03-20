package eu.motogymkhana.competition.api;

import eu.motogymkhana.competition.model.Times;

/**
 * Created by christine on 14-2-16.
 */
public class UpdateTimesRequest extends GymkhanaRequest {

    private Times times;

    public UpdateTimesRequest() {
    }

    public UpdateTimesRequest(Times times) {
        this.times = times;
    }

    public Times getTimes() {
        return times;
    }
}
