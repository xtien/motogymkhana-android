package eu.motogymkhana.competition.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import eu.motogymkhana.competition.model.Country;

public class GymkhanaRequest {

    @JsonProperty("country")
    private Country country;

    @JsonProperty("season")
    private int season;

    @JsonProperty("password")
    private String password;

    public GymkhanaRequest() {
    }

    public GymkhanaRequest(Country country, int season) {
        this.country = country;
        this.season = season;
    }

    public Country getCountry() {
        return country;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
}
