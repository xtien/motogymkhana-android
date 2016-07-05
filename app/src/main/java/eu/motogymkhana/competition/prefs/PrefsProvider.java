/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.prefs;

import android.content.Context;

import roboguice.RoboGuice;

/**
 * Created by christine on 26-5-15.
 * Utility class for enabling injection of RoundManager in non-injected classes.
 */
public class PrefsProvider {

    private static MyPreferences prefs;

    private static Context context;

    public static MyPreferences getInstance() {

        if (prefs == null) {
            prefs = RoboGuice.getInjector(context).getInstance(MyPreferences.class);
        }

        return prefs;
    }

    public static void setContext(Context c) {
        context = c;
    }
}
