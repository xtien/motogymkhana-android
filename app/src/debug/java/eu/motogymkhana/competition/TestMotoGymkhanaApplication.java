/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition;

import android.app.Application;

import eu.motogymkhana.competition.module.GymkhanaModule;
import eu.motogymkhana.competition.module.LiveModule;
import eu.motogymkhana.competition.module.TestLiveServerModule;
import eu.motogymkhana.competition.module.TestModule;
import toothpick.Scope;
import toothpick.Toothpick;

/**
 * Created by christine on 22-4-16.
 */
public class TestMotoGymkhanaApplication extends Application {

    private static final String LOGTAG = TestMotoGymkhanaApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        Scope appScope = Toothpick.openScope(Constants.DEFAULT_SCOPE);
        appScope.installModules(new GymkhanaModule(getApplicationContext()), new LiveModule(getApplicationContext()));

        Scope testScope = Toothpick.openScope(Constants.TEST_SCOPE);
        testScope.installModules(new GymkhanaModule(getApplicationContext()), new TestModule(getApplicationContext()));

        Scope liveTestScope = Toothpick.openScope(Constants.LIVE_TEST_SCOPE);
        liveTestScope.installModules(new GymkhanaModule(getApplicationContext()), new TestLiveServerModule
                (getApplicationContext()));
    }
}
