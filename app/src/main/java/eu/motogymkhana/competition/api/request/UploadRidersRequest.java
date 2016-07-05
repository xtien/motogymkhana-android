/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.api.request;

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
