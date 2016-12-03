/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.liveTest;

import android.content.Context;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.sql.SQLException;
import java.util.Collection;

import javax.inject.Inject;

import eu.motogymkhana.competition.BuildConfig;
import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.http.FakeHttp;
import eu.motogymkhana.competition.model.Round;
import eu.motogymkhana.competition.round.RoundManager;
import toothpick.Scope;
import toothpick.Toothpick;

/**
 * Created by christine on 24-7-15.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class RoundsApiTest {

    @Inject
    protected RoundManager roundManager;

    private String urlString = "https://api.gymcomp.com:9005/motogymkhana/getRounds/";
    private String resultFileName = "test/get_rounds.json";
    private String uploadRoundsUrlString = "https://api.gymcomp.com:9005/motogymkhana/uploadRounds/";
    private String uploadRoundsResultFileName = "test/get_rounds.json";
    private String ridersUrlString = "https://api.gymcomp.com:9005/motogymkhana/getRiders/";
    private String ridersJsonFile = "test/get_riders.json";

    @Test
    public void testGetDates() throws SQLException, InterruptedException {

        Scope scope = Toothpick.openScope(Constants.LIVE_TEST_SCOPE);
        Toothpick.inject(this, scope);

        roundManager.loadRoundsFromServer();

        Collection<Round> rounds = null;

        while (rounds == null || rounds.size() < 8) {
            Thread.sleep(1000);
            rounds = roundManager.getRounds();
        }

        Assert.assertEquals(8, rounds.size());
    }
}
