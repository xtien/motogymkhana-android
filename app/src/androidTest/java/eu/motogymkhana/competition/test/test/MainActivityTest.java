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
import eu.motogymkhana.competition.activity.MainActivity;
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
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

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
    private String ridersUrlString = "https://api.gymcomp.com:9005/motogymkhana/getRiders/";
    private String ridersJsonFile = "test/get_riders.json";

    @Test
    public void testActivity() throws ParseException, SQLException {

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

        fakeHttp.put(ridersUrlString, 200, "", ridersJsonFile);

        onData(anything())
                .inAdapterView(allOf(withId(android.R.id.list), isCompletelyDisplayed()))
                .atPosition(0).perform(click());
        onView(withId(R.id.number))
                .check(matches(withText(containsString("6"))));
    }

    @Test
    public void testActivityFalse() throws ParseException, SQLException {

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

        fakeHttp.put(ridersUrlString, 200, "", ridersJsonFile);

        onData(anything())
                .inAdapterView(allOf(withId(android.R.id.list), isCompletelyDisplayed()))
                .atPosition(0).perform(click());
        onView(withId(R.id.number))
                .check(matches(withText(not(containsString("5")))));
    }
}