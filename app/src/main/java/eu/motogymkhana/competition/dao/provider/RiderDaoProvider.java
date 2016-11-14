/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.dao.provider;

import javax.inject.Inject;
import javax.inject.Provider;

import java.sql.SQLException;

import eu.motogymkhana.competition.dao.RiderDao;
import eu.motogymkhana.competition.db.GymkhanaDatabaseHelper;
import eu.motogymkhana.competition.model.Rider;

/**
 * created by Christine
 * utility class for injecting a dao class
 */
public class RiderDaoProvider implements Provider<RiderDao> {

    protected GymkhanaDatabaseHelper helper;

    @Inject
    public RiderDaoProvider(GymkhanaDatabaseHelper helper){
        this.helper = helper;
    }

    @Override
    public RiderDao get() {

        RiderDao dao = null;

        try {
            dao = (RiderDao) helper.getDAO(Rider.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dao;
    }
}
