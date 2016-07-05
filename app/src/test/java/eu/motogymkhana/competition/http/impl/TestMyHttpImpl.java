/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.http.impl;

import com.google.inject.Inject;

import java.io.IOException;

import eu.motogymkhana.competition.api.RequestParams;
import eu.motogymkhana.competition.api.http.HttpResultWrapper;
import eu.motogymkhana.competition.api.http.impl.MyHttpImpl;
import eu.motogymkhana.competition.http.FakeHttp;
import eu.motogymkhana.competition.http.FakeHttpResult;
import eu.motogymkhana.competition.util.FileAssetManager;

public class TestMyHttpImpl extends MyHttpImpl {

    @Inject
    private FakeHttp fakeHttp;

    @Inject
    private FileAssetManager assetManager;

    public TestMyHttpImpl() {

    }

    @Override
    public HttpResultWrapper get(String urlString, RequestParams params) {

        FakeHttpResult result = fakeHttp.get(urlString);
        HttpResultWrapper httpResultWrapper = null;

        String content = null;
        try {
            content = assetManager.getFileContent(result.getContent());
            httpResultWrapper = new HttpResultWrapper(200, "", content);
        } catch (IOException e) {
            e.printStackTrace();
        }return httpResultWrapper;
    }

    public HttpResultWrapper doPutPost(String method, String urlString, String input, RequestParams params) {

        FakeHttpResult result = fakeHttp.get(urlString);

        HttpResultWrapper httpResultWrapper = null;
        try {
            String content = assetManager.getFileContent(result.getContent());
            httpResultWrapper = new HttpResultWrapper(200, "", content);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return httpResultWrapper;
    }
}