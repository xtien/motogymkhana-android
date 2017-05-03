/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.api.response;

import eu.motogymkhana.competition.model.Rider;

/**
 * Created by christine on 15-5-15.
 */
public class UpdateRiderResponse {

    private int status;
    private Rider rider;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isOK() {
        return status == 0;
    }

    public Rider getRider() {
        return rider;
    }
}
