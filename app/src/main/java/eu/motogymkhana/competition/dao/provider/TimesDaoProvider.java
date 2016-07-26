/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.dao.provider;

import com.google.inject.Inject;
import com.google.inject.Provider;

import java.sql.SQLException;

import eu.motogymkhana.competition.dao.TimesDao;
import eu.motogymkhana.competition.db.GymkhanaDatabaseHelper;
import eu.motogymkhana.competition.model.Times;

/**
 * created by Christine
 * utility class for injecting a dao class
 */
public class TimesDaoProvider implements Provider<TimesDao> {

    @Inject
    private GymkhanaDatabaseHelper helper;

    @Override
    public TimesDao get() {

        TimesDao dao = null;

        try {
            dao = (TimesDao) helper.getDAO(Times.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dao;
    }

}
