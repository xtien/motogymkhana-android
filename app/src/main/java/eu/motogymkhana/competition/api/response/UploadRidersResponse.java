/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.api.response;

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

