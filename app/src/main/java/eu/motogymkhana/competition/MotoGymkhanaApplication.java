/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */


package eu.motogymkhana.competition;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.support.multidex.MultiDex;

import java.lang.ref.WeakReference;

import eu.motogymkhana.competition.prefs.PrefsProvider;
import eu.motogymkhana.competition.robo.GymkhanaModule;
import eu.motogymkhana.competition.robo.LiveModule;
import roboguice.RoboGuice;

/**
 * Created by christine on 7-9-15.
 * MotoGymkhana app is an app that allows timekeeping at Motogymkhana races. It also provides live results to
 * spectators and others. By default, it shows results; when entering an admin password, it makes available
 * additional functions to add race dates, riders and time results.
 * The app communicates with a server, by default this is the server at www.gymcomp.com. The server records results
 * that are uploaded by the app (in admin mode) and distributes race results to app users (in non admin mode). The server
 * also publishes an API through which a web site can show the results.
 * <p>
 * The app usually shows MainActivity, which has a ViewPager to show three fragments (non admin mode) or four fragments
 * (admin mode). Fragments contain registration of riders (admin mode only), start list, day results, season results.
 * <p>
 * From the menu, additional screens and functions are available.
 */
public class MotoGymkhanaApplication extends Application {

    private static WeakReference<Context> instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = new WeakReference<Context>(getApplicationContext());

        PrefsProvider.setContext(getApplicationContext());

        RoboGuice.setUseAnnotationDatabases(true);
        RoboGuice.setupBaseApplicationInjector(this, new GymkhanaModule(), new LiveModule());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            MultiDex.install(this);
        }
    }

    public static Context getContext() {
        return instance.get();
    }
}