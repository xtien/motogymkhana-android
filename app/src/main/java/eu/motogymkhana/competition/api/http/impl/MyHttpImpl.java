/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.api.http.impl;

/**
 * Created by christine on 19-1-16.
 */

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

import eu.motogymkhana.competition.api.RequestParams;
import eu.motogymkhana.competition.api.http.HttpResultWrapper;
import eu.motogymkhana.competition.api.http.MyHttp;
import eu.motogymkhana.competition.log.MyLog;

/**
 * Created by christine on 9-6-15.
 */
@Singleton
public class MyHttpImpl implements MyHttp {

    private static final String LOGTAG = MyHttpImpl.class.getSimpleName();
    private static final String SET_COOKIES_HEADER = "Set-Cookie";
    public static final String COOKIES_HEADER = "Cookie";
    private static final CharSequence COOKIE_ID = "_drive_ugo_session";

    private static int readTimeout = 30000;
    private static int connectTimeout = 30000;

    String authString = "ugo_basic_auth_user:Xg139qTL1JapdNgoFYQ1";

    @Inject
    private Context context;

    @Inject
    private MyLog log;

    @Override
    public HttpResultWrapper get(String urlString, RequestParams params) throws IOException {
        return doPutPost("GET", urlString, null, params);
    }

    @Override
    public HttpResultWrapper post(String urlString, String input, RequestParams params) throws IOException {
        return doPutPost("POST", urlString, input, params);
    }

    @Override
    public HttpResultWrapper put(String urlString, RequestParams params) throws IOException {
        return doPutPost("PUT", urlString, null, params);
    }

    @Override
    public HttpResultWrapper delete(String urlString, RequestParams params) throws IOException {
        return doPutPost("DELETE", urlString, null, params);
    }

    @Override
    public HttpResultWrapper update(String urlString, RequestParams params) throws IOException {
        return doPutPost("UPDATE", urlString, null, params);
    }

    @Override
    public HttpResultWrapper doPutPost(String method, String urlString, String input, RequestParams params) throws
            IOException {

        log.d(LOGTAG, "urlString " + method + " " + urlString + " " + input);

        if (params == null) {
            params = new RequestParams();
        }

        String resultString = null;

        String string = "";
        int httpResult = 0;
        String responseMessage = null;

        URL url = new URL(urlString + getQuery(params));

        HttpURLConnection urlConnection = null;

        urlConnection = (HttpURLConnection) url.openConnection();

        try {

            urlConnection.setReadTimeout(readTimeout);
            urlConnection.setConnectTimeout(connectTimeout);

            urlConnection.setRequestMethod(method);
            if (!"GET".equals(method)) {
                urlConnection.setDoOutput(true);
            }
            urlConnection.setUseCaches(false);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");

            urlConnection.connect();

            if (input != null) {
                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());

                if (input != null) {
                    wr.write(input);
                }

                wr.flush();
                wr.close();
            }

            httpResult = urlConnection.getResponseCode();
            if (httpResult != HttpURLConnection.HTTP_OK) {
                responseMessage = urlConnection.getResponseMessage();
            }

            log.d(LOGTAG, "url = " + urlString + " statuscode " + httpResult);

            BufferedReader br = null;
            if (httpResult == HttpURLConnection.HTTP_OK) {
                br = new BufferedReader(new InputStreamReader(new BufferedInputStream(urlConnection.getInputStream())));
            } else {
                br = new BufferedReader(new InputStreamReader(new BufferedInputStream(urlConnection.getErrorStream())));
            }

            if (br != null) {
                String line;
                while ((line = br.readLine()) != null) {
                    string = string + line;
                }
            }

            resultString = string;

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        log.d(LOGTAG, "response PUT/POST " + responseMessage + "\n\n" + string);

        return new HttpResultWrapper(httpResult, responseMessage, resultString);
    }

    private String getQuery(RequestParams params) throws UnsupportedEncodingException {

        if (params != null && params.size() > 0) {
            StringBuilder result = new StringBuilder();
            boolean first = true;

            for (Pair<String, String> pair : params.getParams()) {

                if (pair.first != null && pair.second != null) {

                    if (first) {
                        first = false;
                        result.append("?");

                    } else {
                        result.append("&");
                    }
                    result.append(URLEncoder.encode(pair.first, "UTF-8"));
                    result.append("=");
                    result.append(URLEncoder.encode(pair.second, "UTF-8"));
                }
            }

            return result.toString();
        } else {
            return "";
        }
    }
}
