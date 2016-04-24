package eu.motogymkhana.competition.model;

/**
 * Created by christine on 31-5-15.
 */
public enum Bib {

    Y(""), G("G"), B("B"), R("R");

    private String string;

    Bib(String string) {
        this.string = string;
    }

    public String displayString(){
        return string;
    }
}
