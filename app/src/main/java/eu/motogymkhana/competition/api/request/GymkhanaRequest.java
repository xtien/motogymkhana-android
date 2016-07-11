/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.api.request;

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

    public void setSeason(int season) {
        this.season = season;
    }
}
