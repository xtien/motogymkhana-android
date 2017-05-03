/*
 * Copyright (c) 2015 - 2017, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.dao.provider;

import java.sql.SQLException;

import javax.inject.Inject;
import javax.inject.Provider;

import eu.motogymkhana.competition.dao.RegistrationDao;
import eu.motogymkhana.competition.dao.TimesDao;
import eu.motogymkhana.competition.db.GymkhanaDatabaseHelper;
import eu.motogymkhana.competition.model.Registration;
import eu.motogymkhana.competition.model.Times;

/**
 * created by Christine
 * utility class for injecting a dao class
 */
public class RegistrationDaoProvider implements Provider<RegistrationDao> {

    protected GymkhanaDatabaseHelper helper;

    @Inject
    public RegistrationDaoProvider(GymkhanaDatabaseHelper helper){
        this.helper = helper;
    }

    @Override
    public RegistrationDao get() {

        RegistrationDao dao = null;

        try {
            dao = (RegistrationDao) helper.getDAO(Registration.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dao;
    }
}
