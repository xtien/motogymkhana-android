/*
 * Copyright (c) 2015 - 2017, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.dao;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import eu.motogymkhana.competition.model.Bib;
import eu.motogymkhana.competition.model.Country;
import eu.motogymkhana.competition.model.Registration;
import eu.motogymkhana.competition.model.Rider;
import eu.motogymkhana.competition.model.Times;

public interface RegistrationDao extends Dao<Registration, Integer> {

    void storeRiderRegistrations(Rider rider) throws SQLException;

    void store(Registration registration) throws SQLException;
}
