/*
 * Copyright (c) 2015 - 2017, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.liveTest;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLooper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import eu.motogymkhana.competition.BuildConfig;
import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.api.ResponseHandler;
import eu.motogymkhana.competition.api.response.ListRidersResult;
import eu.motogymkhana.competition.model.Country;
import eu.motogymkhana.competition.model.Registration;
import eu.motogymkhana.competition.model.Rider;
import eu.motogymkhana.competition.rider.RiderManager;
import toothpick.Scope;
import toothpick.Toothpick;

/**
 * Created by christine on 25-3-17.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class RegisterRiderApiTest {

    @Inject
    protected RiderManager riderManager;

    private boolean done = false;
    private List<Rider> riders = new ArrayList<Rider>();

    private ResponseHandler responseHandler = new ResponseHandler() {

        @Override
        public void onSuccess(Object object) {
            ListRidersResult result = (ListRidersResult) object;
            riders.addAll(result.getRiders());
            done = true;
        }

        @Override
        public void onException(Exception e) {
            done = true;
        }

        @Override
        public void onError(int statusCode, String string) {
            done = true;
        }
    };

    @Test
    public void registerRiderTest() throws InterruptedException {

        Scope scope = Toothpick.openScope(Constants.LIVE_TEST_SCOPE);
        Toothpick.inject(this, scope);

        riderManager.getAllRiders(responseHandler);

        while (!done) {
            Thread.sleep(1000);
            ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
        }
        done = false;

        Assert.assertEquals(105, riders.size());

        Rider rider = riders.get(80);

        Assert.assertEquals(8, rider.getRegistrations().size());

        Registration registration = rider.getRegistration(Country.NL, 2016);

        Assert.assertNotNull(registration);

    }
}
