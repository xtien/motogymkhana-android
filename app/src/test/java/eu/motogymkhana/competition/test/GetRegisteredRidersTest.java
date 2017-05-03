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
import java.util.List;

import javax.inject.Inject;

import eu.motogymkhana.competition.BuildConfig;
import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.dao.RegistrationDao;
import eu.motogymkhana.competition.dao.RiderDao;
import eu.motogymkhana.competition.dao.RoundDao;
import eu.motogymkhana.competition.dao.TimesDao;
import eu.motogymkhana.competition.model.Bib;
import eu.motogymkhana.competition.model.Country;
import eu.motogymkhana.competition.model.Registration;
import eu.motogymkhana.competition.model.Rider;
import eu.motogymkhana.competition.model.Round;
import eu.motogymkhana.competition.model.Times;
import toothpick.Scope;
import toothpick.Toothpick;

/**
 * Created by christine on 27-3-17.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(packageName = "eu.motogymkhana.competition", constants = BuildConfig.class, sdk = 21)
public class GetRegisteredRidersTest {

    @Inject
    protected TimesDao timesDao;

    @Inject
    protected RoundDao roundDao;

    @Inject
    protected RiderDao riderDao;

    @Inject
    protected RegistrationDao registrationDao;

    @Test
    public void testGetRegisteredRiders() throws SQLException {

        Scope scope = Toothpick.openScope(Constants.TEST_SCOPE);
        Toothpick.inject(this, scope);

        long now = System.currentTimeMillis();

        Constants.season = 2017;
        Constants.country = Country.NL;

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

        Rider rider2 = new Rider();
        Registration registration2 = new Registration(Country.NL, 2017, 2);
        registration2.setRegistered(true);
        registration2.setRider(rider2);
        registration2.setBib(Bib.Y);
        rider2.addRegistration(registration2);

        Times times2 = new Times();
        times2.setRider(rider2);
        times2.setRegistered(true);
        times2.setDate(now);
        times2.setCountry(Country.NL);
        times2.setSeason(2017);
        rider2.addTimes(times2);
        riderDao.store(rider2);

        Rider rider3 = new Rider();
        Registration registration3 = new Registration(Country.NL, 2017, 3);
        registration3.setRegistered(true);
        registration3.setRider(rider3);
        registration3.setBib(Bib.G);
        rider3.addRegistration(registration3);

        Times times3 = new Times();
        times3.setRider(rider3);
        times3.setRegistered(true);
        times3.setDate(now);
        times3.setCountry(Country.NL);
        times3.setSeason(2017);
        rider3.addTimes(times3);
        riderDao.store(rider3);

        Round round = roundDao.getRoundByNumber(8);

        List<Rider> list = riderDao.getRiders();

        Assert.assertEquals(3,list.size());

        List<Rider> riders = timesDao.getRegisteredRiders(round.getDate());
        Assert.assertEquals(3, riders.size());

        List<Rider> riders2 = timesDao.getRegisteredRiders(round.getDate(),Bib.Y);
        Assert.assertEquals(3, riders2.size());

    }
}
