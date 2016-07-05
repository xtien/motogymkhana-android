/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.api.http;

import java.io.IOException;

import eu.motogymkhana.competition.api.RequestParams;

/**
 * Created by christine on 21-1-16.
 */
public interface MyHttp {

    HttpResultWrapper get(String urlString, RequestParams params) throws IOException;

    HttpResultWrapper post(String checkEmailUrl, String input, RequestParams params) throws IOException;

    HttpResultWrapper put(String urlString, RequestParams params) throws IOException;

    HttpResultWrapper delete(String urlString, RequestParams params) throws IOException;

    HttpResultWrapper update(String urlString, RequestParams params) throws IOException;

    HttpResultWrapper doPutPost(String method, String urlString, String input, RequestParams params) throws
            IOException;
}
