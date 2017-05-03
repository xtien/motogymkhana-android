/*
 * Copyright (c) 2015 - 2017, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.test;

import android.content.Context;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLooper;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import eu.motogymkhana.competition.BuildConfig;
import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.api.ResponseHandler;
import eu.motogymkhana.competition.api.response.ListRidersResult;
import eu.motogymkhana.competition.http.FakeHttp;
import eu.motogymkhana.competition.model.Rider;
import eu.motogymkhana.competition.rider.RiderManager;
import toothpick.Scope;
import toothpick.Toothpick;

/**
 * Created by christine on 26-3-17.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(packageName = "eu.motogymkhana.competition", constants = BuildConfig.class, sdk = 21)
public class AddRiderTest {

    @Inject
    protected RiderManager riderManager;

    @Inject
    protected Context context;

    @Inject
    protected FakeHttp fakeHttp;

    private Collection<Rider> allRiders;
    private boolean done  = false;

    private String ridersUrlString = "https://pengo.christine.nl:9005/motogymkhana/getAllRiders/";
    private String ridersJsonFile = "test/all_riders_result.json";

    private ResponseHandler responseHandler = new ResponseHandler() {

        @Override
        public void onSuccess(Object object) {
            ListRidersResult result = (ListRidersResult)object;
            allRiders = result.getRiders();
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
    public void addRiderTest() throws InterruptedException {

        Scope scope = Toothpick.openScope(Constants.TEST_SCOPE);
        Toothpick.inject(this, scope);
        Assert.assertNotNull(context);

        fakeHttp.put(ridersUrlString, 200, "", ridersJsonFile);

        riderManager.getAllRiders(responseHandler);

        while (!done) {
            Thread.sleep(100);
            ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
        }

        Assert.assertEquals(105, allRiders.size());
    }
}
