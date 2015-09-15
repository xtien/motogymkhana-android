package eu.motogymkhana.competition.api;

/**
 * Created by christine on 15-5-15.
 */
public class UploadRidersResponse {

    private int status;
    private int numberOfRiders;
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public int getNumberOfRiders() {
        return numberOfRiders;
    }
    public void setNumberOfRiders(int numberOfRiders) {
        this.numberOfRiders = numberOfRiders;
    }

}

