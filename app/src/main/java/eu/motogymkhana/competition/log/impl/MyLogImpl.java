/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.log.impl;

import android.util.Log;

import com.google.inject.Singleton;

import eu.motogymkhana.competition.BuildConfig;
import eu.motogymkhana.competition.log.LogProvider;
import eu.motogymkhana.competition.log.MyLog;

/**
 * Created by christine on 13-6-16.
 */
@Singleton
public class MyLogImpl implements MyLog {

    private boolean logging = false;

    public MyLogImpl() {

        if (BuildConfig.DEBUG) {
            logging = true;
        } else {
            logging = false;
        }
        LogProvider.setLogger(this);
    }

    @Override
    public void i(String tag, String message) {
        if (logging) {
            Log.i(tag, message);
        }
    }

    @Override
    public void d(String tag, String message) {
        if (logging) {
            Log.d(tag, message);
        }
    }

    @Override
    public void e(String tag, String message) {
        if (logging) {
            Log.e(tag, message);
        }
    }

    @Override
    public void e(String tag, Exception e) {
        if (logging) {
            e.printStackTrace();
        }
    }
}
