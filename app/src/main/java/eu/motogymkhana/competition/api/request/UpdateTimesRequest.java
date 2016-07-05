/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.api.request;

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
