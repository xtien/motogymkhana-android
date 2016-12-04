/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.test.test;

import android.content.Context;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.sql.SQLException;
import java.text.ParseException;

import javax.inject.Inject;

import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.R;
import eu.motogymkhana.competition.activity.SettingsActivity;
import eu.motogymkhana.competition.dao.RiderDao;
import eu.motogymkhana.competition.dao.RoundDao;
import eu.motogymkhana.competition.http.FakeHttp;
import eu.motogymkhana.competition.model.Round;
import toothpick.Scope;
import toothpick.Toothpick;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class SettingsActivityTest {

    @Rule
    public ActivityTestRule<SettingsActivity> activityTestRule = new ActivityTestRule<>(SettingsActivity.class);

    @Inject
    protected RiderDao riderDao;

    @Inject
    protected RoundDao roundDao;

    @Inject
    protected FakeHttp fakeHttp;

    @Inject
    protected Context context;

    String dateOne = "01-06-2016";
    String dateTwo = "01-07-2016";

    private String uploadRoundsUrlString = "https://api.gymcomp.com:9005/motogymkhana/uploadRounds/";
    private String uploadRoundsResultFileName = "test/get_rounds.json";

    @Test
    public void testSpinner() throws SQLException, ParseException {
        Scope scope = Toothpick.openScope(Constants.TEST_SCOPE);
        Toothpick.inject(this, scope);

        Assert.assertNotNull(context);

        Round roundOne = new Round();
        roundOne.setDate(Constants.dateFormat.parse(dateOne).getTime());
        roundOne.setCountry(Constants.country);
        roundOne.setSeason(Constants.season);
        Round roundTwo = new Round();
        roundTwo.setDate(Constants.dateFormat.parse(dateTwo).getTime());
        roundTwo.setCountry(Constants.country);
        roundTwo.setSeason(Constants.season);

        roundDao.store(roundOne);
        roundDao.store(roundTwo);

        fakeHttp.put(uploadRoundsUrlString, 200, "", uploadRoundsResultFileName);

        onView(withId(R.id.country_spinner))
                .perform(click());
        onData(allOf(is(instanceOf(String.class)), is("NL")))
                .perform(click());
        onView(withId(R.id.country_spinner))
                .check(matches(withSpinnerText(containsString("NL"))));
    }
}