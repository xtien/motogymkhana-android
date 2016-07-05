/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.api.request;

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
