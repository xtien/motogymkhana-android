/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import eu.motogymkhana.competition.model.Country;

/**
 * created by Christine
 * Constants for the app.
 */
public class Constants {

    public static final boolean useTestServer = true;
    public static boolean USE_HTTPS = true;
    public static final String testServer = "pengo.christine.nl";
    public static final String productionServer = "api.gymcomp.com";

    public static final long refreshRate = 5000l;
    public static boolean test = false;
    public static final int DATABASE_VERSION = 9;
    public static String DATABASE_NAME = "gymkhana.db";

    public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    public static DateFormat dateFormatSeconds = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    public static final String USER_AGENT = "motogymkhana_android";
    public static final String urlHttp = "http://";
    public static final String urlHttps = "https://";
    public static final String hostName = useTestServer ? testServer : productionServer;
    public static final int HTTPS_PORT = 9005;
    public static final int HTTP_PORT = 8085;
    public static final String BASE_URL_STRING = "/motogymkhana";

    public static final String GET_RIDERS_URL_STRING = BASE_URL_STRING + "/getRiders/";
    public static final String GET_ROUNDS_URL_STRING = BASE_URL_STRING + "/getRounds/";
    public static final String GET_UPDATE_ROUND_URL_STRING = BASE_URL_STRING + "/updateRound/";
    public static final String UPDATE_RIDER_URL_STRING = BASE_URL_STRING + "/updateRider/";
    public static final String UPDATE_TIMES_URL_STRING = BASE_URL_STRING + "/updateTimes/";
    public static final String UPDATE_RIDERS_URL_STRING = BASE_URL_STRING + "/updateRiders/";
    public static final String UPLOAD_ROUNDS_URL_STRING = BASE_URL_STRING + "/uploadRounds/";
    public static final String DELETE_RIDER_URL_STRING = BASE_URL_STRING + "/deleteRider/";
    public static final String LOAD_RIDERS_URL_STRING = BASE_URL_STRING + "/uploadRiders/";
    public static final String SEND_TEXT_URL_STRING = BASE_URL_STRING + "/updateText/";
    public static final String CHECK_PASSWORD = BASE_URL_STRING + "/pw/";

    public static final String UPLOAD_SETTINGS_URL_STRING = BASE_URL_STRING + "/updateSettings/";
    public static final String GET_SETTINGS_URL_STRING = BASE_URL_STRING + "/getSettings/";

    public static Country country = Country.NL;
    public static int season = 2016;
}
