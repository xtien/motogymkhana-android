/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition;

import android.app.Application;

import eu.motogymkhana.competition.robo.GymkhanaModule;
import eu.motogymkhana.competition.robo.TestModule;
import roboguice.RoboGuice;

/**
 * Created by christine on 22-4-16.
 */
public class TestMotoGymkhanaApplication extends Application {

    private static final String LOGTAG = TestMotoGymkhanaApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        RoboGuice.setUseAnnotationDatabases(false);
        RoboGuice.setupBaseApplicationInjector(this, new GymkhanaModule(), new TestModule());
    }
}
