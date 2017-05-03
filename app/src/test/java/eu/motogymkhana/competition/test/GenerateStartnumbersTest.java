/*
 * Copyright (c) 2015 - 2017, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.test;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLooper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import eu.motogymkhana.competition.BuildConfig;
import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.api.ResponseHandler;
import eu.motogymkhana.competition.api.response.UploadRidersResponse;
import eu.motogymkhana.competition.dao.RegistrationDao;
import eu.motogymkhana.competition.dao.RiderDao;
import eu.motogymkhana.competition.dao.RoundDao;
import eu.motogymkhana.competition.model.Bib;
import eu.motogymkhana.competition.model.Country;
import eu.motogymkhana.competition.model.Registration;
import eu.motogymkhana.competition.model.Rider;
import eu.motogymkhana.competition.model.Round;
import eu.motogymkhana.competition.model.Times;
import eu.motogymkhana.competition.prefs.MyPreferences;
import eu.motogymkhana.competition.rider.RiderManager;
import toothpick.Scope;
import toothpick.Toothpick;

/**
 * Created by christine on 2-4-17.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(packageName = "eu.motogymkhana.competition", constants = BuildConfig.class, sdk = 21)
public class GenerateStartnumbersTest {

    @Inject
    protected RiderDao riderDao;

    @Inject
    protected RoundDao roundDao;

    @Inject
    protected RiderManager riderManager;

    @Inject
    protected RegistrationDao registrationDao;

    @Inject
    protected MyPreferences prefs;

    private long now;
    private boolean done = false;

    @Test
    public void testGenerateStartNumbers() throws SQLException, InterruptedException {

        Scope scope = Toothpick.openScope(Constants.TEST_SCOPE);
        Toothpick.inject(this, scope);

        now = System.currentTimeMillis();
        Constants.season = 2017;
        Constants.country = Country.NL;

        prefs.setDate(now);

        Round round1 = new Round();
        round1.setCountry(Country.NL);
        round1.setSeason(2017);
        round1.setDate(now);
        round1.setNumber(8);
        roundDao.store(round1);

        Rider rider1 = createRider(Bib.Y, true);
        Rider rider2 = createRider(Bib.Y, true);
        Rider rider3 = createRider(Bib.Y, true);
        Rider rider4 = createRider(Bib.Y, true);
        Rider rider5 = createRider(Bib.Y, true);
        Rider rider6 = createRider(Bib.Y, true);
        Rider rider7 = createRider(Bib.B, true);
        Rider rider8 = createRider(Bib.B, true);
        Rider rider9 = createRider(Bib.G, true);
        Rider rider10 = createRider(Bib.G, true);

        riderManager.generateStartNumbers();

        List<Rider> riders = riderDao.getRiders();

        Assert.assertNotNull(riders);
        Assert.assertEquals(10, riders.size());
        List<Integer> numbers = new ArrayList<Integer>();
        for (Rider r : riders) {
            for (Times t : r.getTimes()) {
                numbers.add(t.getStartNumber());
            }
        }

        Assert.assertEquals(10, numbers.size());
        Assert.assertTrue(numbers.contains(6));
        Assert.assertTrue(numbers.contains(3));
        Assert.assertTrue(numbers.contains(1));
        Assert.assertTrue(numbers.contains(101));
        Assert.assertTrue(numbers.contains(102));
        Assert.assertTrue(numbers.contains(201));
        Assert.assertTrue(numbers.contains(202));
    }

    private Rider createRider(Bib bib, boolean registered) throws SQLException {

        Rider rider = new Rider();
        Registration registration = new Registration(Country.NL, 2017, registered);
        registration.setRider(rider);
        registration.setBib(bib);
        rider.addRegistration(registration);

        Times times1 = new Times();
        times1.setRider(rider);
        times1.setRegistered(true);
        times1.setDate(now);
        times1.setCountry(Country.NL);
        times1.setSeason(2017);
        rider.addTimes(times1);
        riderDao.store(rider);

        return rider;
    }
}
