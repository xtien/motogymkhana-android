/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.round;

import android.content.Context;

import roboguice.RoboGuice;

/**
 * Created by christine on 26-5-15.
 * Utility class for enabling injection of RoundManager in non-injected classes.
 */
public class RoundManagerProvider {

    private static RoundManager roundManager;

    private static Context context;

    public static RoundManager getInstance() {

        if (roundManager == null) {
            roundManager = RoboGuice.getInjector(context).getInstance(RoundManager.class);
        }

        return roundManager;
    }

    public static void setContext(Context c) {
        context = c;
    }
}
