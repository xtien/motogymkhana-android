/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.country;

import java.util.Collection;

import eu.motogymkhana.competition.model.Country;

/**
 * Created by christine on 10-1-16.
 */
public interface CountryManager {

    Collection<Country> getCountries();
}
