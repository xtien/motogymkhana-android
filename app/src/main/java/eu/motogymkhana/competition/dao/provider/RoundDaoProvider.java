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

import eu.motogymkhana.competition.dao.RoundDao;
import eu.motogymkhana.competition.db.GymkhanaDatabaseHelper;
import eu.motogymkhana.competition.model.Round;

/**
 * created by Christine
 * utility class for injecting a dao class
 */
public class RoundDaoProvider implements Provider<RoundDao> {


    protected GymkhanaDatabaseHelper helper;

    @Inject
    public RoundDaoProvider(GymkhanaDatabaseHelper helper){
        this.helper = helper;
    }

    @Override
    public RoundDao get() {

        RoundDao dao = null;

        try {
            dao = (RoundDao) helper.getDAO(Round.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dao;
    }

}
