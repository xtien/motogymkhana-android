/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.api;

/**
 * Created by christine on 21-1-16.
 */
public interface ResponseHandler {

    void onSuccess(Object object);

    void onException(Exception e);

    void onError(int statusCode, String string);
}
