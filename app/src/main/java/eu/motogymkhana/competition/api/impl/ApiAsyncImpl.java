/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.api.impl;

import android.os.AsyncTask;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.inject.Inject;
import javax.inject.Singleton;

import java.util.concurrent.ExecutorService;

import eu.motogymkhana.competition.api.ApiAsync;
import eu.motogymkhana.competition.api.RequestParams;
import eu.motogymkhana.competition.api.ResponseHandler;
import eu.motogymkhana.competition.api.executor.ExecutorFactory;
import eu.motogymkhana.competition.api.http.HttpResultWrapper;
import eu.motogymkhana.competition.api.http.MyHttp;

/**
 * Created by christine on 2-6-16.
 */
@Singleton
public class ApiAsyncImpl implements ApiAsync {

    @Inject
    protected ObjectMapper mapper;

    @Inject
    protected MyHttp http;

    private final ExecutorService es;

    @Inject
    public ApiAsyncImpl() {
        es = ExecutorFactory.get();
    }

    @Override
    public void get(final String urlString, final RequestParams params, final ResponseHandler responseHandler,
                    final Object clazz) {
        putPost("GET", urlString, null, params, responseHandler, clazz);
    }

    @Override
    public void post(final String urlString, final String input, final RequestParams params, final ResponseHandler
            responseHandler,
                     final Object clazz) {
        putPost("POST", urlString, input, params, responseHandler, clazz);
    }

    @Override
    public void post(final String urlString, final String input, final ResponseHandler responseHandler,
                     final Object clazz) {
        putPost("POST", urlString, input, null, responseHandler, clazz);
    }

    @Override
    public void put(final String urlString, final ResponseHandler responseHandler,
                    final Object clazz) {
        putPost("PUT", urlString, null, null, responseHandler, clazz);
    }

    private void putPost(final String method,
                         final String urlString,
                         final String input,
                         final RequestParams params,
                         final ResponseHandler responseHandler,
                         final Object clazz) {

        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... args) {

                try {
                    HttpResultWrapper result;

                    result = http.doPutPost(method, urlString, input, params);

                    if (responseHandler != null) {

                        if (result.getStatusCode() == 200) {

                            if (clazz != null) {
                                if (clazz instanceof Class) {
                                    responseHandler.onSuccess(mapper.readValue(result.getString(), (Class<?>) clazz));
                                } else {
                                    responseHandler.onSuccess(mapper.readValue(result.getString(), (JavaType) clazz));
                                }
                            } else {
                                responseHandler.onSuccess(result.getString());
                            }

                        } else if (result.getString() != null) {
                            responseHandler.onError(result.getStatusCode(), result.getString());
                        }
                    }

                } catch (Exception e) {
                    if (responseHandler != null) {
                        responseHandler.onException(e);
                    }
                    e.printStackTrace();
                }
                return null;
            }

        }.executeOnExecutor(es);
    }

    @Override
    public void put(String urlString, RequestParams params, ResponseHandler responseHandler, Object clazz) {
        putPost("PUT", urlString, null, params, responseHandler, clazz);
    }

    @Override
    public void delete(String urlString, ResponseHandler responseHandler, Object clazz) {
        putPost("DELETE", urlString, null, null, responseHandler, clazz);
    }

    @Override
    public void update(String urlString, ResponseHandler responseHandler, Object clazz) {
        putPost("UPDATE", urlString, null, null, responseHandler, clazz);
    }
}
