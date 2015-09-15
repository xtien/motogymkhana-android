package eu.motogymkhana.competition.api;

/**
 * Created by christine on 19-5-15.
 */
public class GymkhanaResult {

    private int resultCode;

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }
}