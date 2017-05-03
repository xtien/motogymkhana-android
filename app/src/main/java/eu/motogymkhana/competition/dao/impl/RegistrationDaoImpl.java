/*
 * Copyright (c) 2015 - 2017, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.dao.impl;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

import eu.motogymkhana.competition.dao.RegistrationDao;
import eu.motogymkhana.competition.model.Country;
import eu.motogymkhana.competition.model.Registration;
import eu.motogymkhana.competition.model.Rider;

public class RegistrationDaoImpl extends BaseDaoImpl<Registration, Integer> implements RegistrationDao {

    private static final String LOGTAG = RegistrationDaoImpl.class.getSimpleName();

    public RegistrationDaoImpl(ConnectionSource connectionSource, Class<Registration> dataClass) throws SQLException {
        super(connectionSource, Registration.class);
    }

    @Override
    public void storeRiderRegistrations(Rider rider) throws SQLException {

        for (Registration registration : rider.getRegistrations()) {
            registration.setRider(rider);
            store(registration);
        }
    }

    @Override
    public void store(Registration registration) throws SQLException {

        if (registration.getRider() == null) {
            throw new IllegalStateException("registration should have a rider");
        }

        Registration existingRegistration = getRegistration(registration.getRider().get_id(), registration.getCountry
                (), registration.getSeason());
        if (existingRegistration != null) {
            registration.set_id(existingRegistration.get_id());
             update(registration);
            return;

        } else {
            create(registration);
        }
    }

    private Registration getRegistration(int id, Country country, int season) throws SQLException {

        QueryBuilder<Registration, Integer> statementBuilder = queryBuilder();
        statementBuilder.where().eq(Registration.RIDER, id).and().eq(Registration.COUNTRY, country).and().eq
                (Registration.SEASON, season);

        List<Registration> list = query(statementBuilder.prepare());

        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }
}
