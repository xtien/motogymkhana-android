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

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import eu.motogymkhana.competition.BuildConfig;
import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.adapter.ChangeListener;
import eu.motogymkhana.competition.api.ApiManager;
import eu.motogymkhana.competition.api.ResponseHandler;
import eu.motogymkhana.competition.api.response.ListRidersResult;
import eu.motogymkhana.competition.api.response.UpdateRiderResponse;
import eu.motogymkhana.competition.api.response.UploadRidersResponse;
import eu.motogymkhana.competition.dao.RiderDao;
import eu.motogymkhana.competition.dao.RoundDao;
import eu.motogymkhana.competition.model.Rider;
import eu.motogymkhana.competition.model.Times;
import eu.motogymkhana.competition.notify.Notifier;
import eu.motogymkhana.competition.rider.GetRidersCallback;
import eu.motogymkhana.competition.rider.RiderManager;
import toothpick.Scope;
import toothpick.Toothpick;

/**
 * Created by christine on 24-7-15.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class UpdateRiderTest {

    @Inject
    protected RiderManager riderManager;

    @Inject
    protected RoundDao roundDao;

    @Inject
    protected Notifier notifier;

    @Inject
    protected ApiManager apiManager;

    @Inject
    protected RiderDao riderDao;

    private volatile boolean done = false;
    private Rider updatedRider;

    private List<Rider> riders = new LinkedList<Rider>();
    private Collection<Rider> downloadedRiders;

    private ResponseHandler downloadRidersResponseHandler = new ResponseHandler() {

        @Override
        public void onSuccess(Object object) {

            downloadedRiders = ((ListRidersResult) object).getRiders();

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

    private ResponseHandler riderResponseHandler = new ResponseHandler() {
        @Override
        public void onSuccess(Object object) {
            UpdateRiderResponse response = (UpdateRiderResponse) object;
            updatedRider = response.getRider();
        }

        @Override
        public void onException(Exception e) {

        }

        @Override
        public void onError(int statusCode, String string) {

        }
    };

    @Test
    public void testGetRiders() throws IOException, InterruptedException, ParseException, SQLException {

        Scope scope = Toothpick.openScope(Constants.LIVE_TEST_SCOPE);
        Toothpick.inject(this, scope);

        notifier.registerRiderResultListener(new ChangeListener() {

            @Override
            public void notifyDataChanged() {
                done = true;
            }
        });

        riderManager.downloadRiders(downloadRidersResponseHandler);

        while (!done) {
            Thread.sleep(1000);
        }
        Assert.assertEquals(31, downloadedRiders.size());

        Rider rider = downloadedRiders.iterator().next();

        int nrOfTimes = rider.getTimes().size();

        for(Times t : rider.getTimes()){
            t.setStartNumber(9);
        }

        done = false;
        apiManager.updateRider(rider, riderResponseHandler);

        while (!done) {
            Thread.sleep(1000);
        }

        Assert.assertNotNull(updatedRider);
        Assert.assertEquals(nrOfTimes,updatedRider.getTimes().size());
    }
}
