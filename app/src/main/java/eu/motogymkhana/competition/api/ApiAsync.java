/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.api;

/**
 * Created by christine on 2-6-16.
 */
public interface ApiAsync {

    void get(String urlString, RequestParams params, ResponseHandler responseHandler,
             Object clazz);

    void post(String urlString, String input, RequestParams params, ResponseHandler responseHandler,
              Object clazz);

    void post(String urlString, String input, ResponseHandler responseHandler,
              Object clazz);

    void put(String urlString, ResponseHandler responseHandler,
             Object clazz);

    void put(String urlString, RequestParams params, ResponseHandler responseHandler, Object clazz);

    void delete(String urlString, ResponseHandler responseHandler, Object clazz);

    void update(String urlString, ResponseHandler responseHandler, Object clazz);
}
