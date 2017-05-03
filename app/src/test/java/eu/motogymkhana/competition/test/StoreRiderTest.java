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

import java.sql.SQLException;

import javax.inject.Inject;

import eu.motogymkhana.competition.BuildConfig;
import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.dao.RiderDao;
import eu.motogymkhana.competition.dao.RoundDao;
import eu.motogymkhana.competition.model.Bib;
import eu.motogymkhana.competition.model.Country;
import eu.motogymkhana.competition.model.Registration;
import eu.motogymkhana.competition.model.Rider;
import eu.motogymkhana.competition.model.Round;
import eu.motogymkhana.competition.model.Times;
import toothpick.Scope;
import toothpick.Toothpick;

/**
 * Created by christine on 26-3-17.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(packageName = "eu.motogymkhana.competition", constants = BuildConfig.class, sdk = 21)
public class StoreRiderTest {

    @Inject
    protected RiderDao riderDao;

    @Inject
    protected RoundDao roundDao;

    @Test
    public void testStoreRider() throws SQLException {

        Scope scope = Toothpick.openScope(Constants.TEST_SCOPE);
        Toothpick.inject(this, scope);

        long now = System.currentTimeMillis();

        Round round1 = new Round();
        round1.setCountry(Country.NL);
        round1.setSeason(2017);
        round1.setDate(now);
        round1.setNumber(8);
        roundDao.store(round1);

        Rider rider1 = new Rider();
        Registration registration1 = new Registration(Country.NL, 2017, 1);
        registration1.setRider(rider1);
        registration1.setRegistered(true);
        registration1.setBib(Bib.Y);
        rider1.addRegistration(registration1);

        Times times1 = new Times();
        times1.setRider(rider1);
        times1.setRegistered(true);
        times1.setDate(now);
        times1.setCountry(Country.NL);
        times1.setSeason(2017);
        rider1.addTimes(times1);
        riderDao.store(rider1);

        Rider resultRider = riderDao.getRiderById(rider1.get_id());

        Assert.assertNotNull(resultRider);
        Assert.assertEquals(1,resultRider.getTimes().size());

        Times times4 = new Times();
        times4.setRider(rider1);
        times4.setRegistered(true);
        times4.setDate(now);
        times4.setCountry(Country.NL);
        times4.setSeason(2017);
        resultRider.addTimes(times4);
        riderDao.store(resultRider);

        Assert.assertEquals(2,resultRider.getTimes().size());

    }
}
