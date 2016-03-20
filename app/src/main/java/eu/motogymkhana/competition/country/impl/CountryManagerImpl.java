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
