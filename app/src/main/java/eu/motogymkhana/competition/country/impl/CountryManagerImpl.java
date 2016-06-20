/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.country.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import eu.motogymkhana.competition.country.CountryManager;
import eu.motogymkhana.competition.model.Country;

/**
 * Created by christine on 10-1-16.
 */
public class CountryManagerImpl implements CountryManager {

    private List<Country> countries = new ArrayList<Country>();

    public CountryManagerImpl() {
        countries.add(Country.NL);
        countries.add(Country.EU);
        countries.add(Country.BE);
        countries.add(Country.UA);
    }

    @Override
    public Collection<Country> getCountries() {
        return null;
    }
}
