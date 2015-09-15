package eu.motogymkhana.competition.api;

/**
 * Created by christine on 20-5-15.
 */
public class UpdateTextRequest extends GymkhanaRequest {

    private String text;

    public UpdateTextRequest(String text){
        this.text=text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UpdateTextRequest(){

    }
}