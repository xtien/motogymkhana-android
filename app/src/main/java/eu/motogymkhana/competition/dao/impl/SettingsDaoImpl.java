package eu.motogymkhana.competition.dao.impl;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.dao.CredentialDao;
import eu.motogymkhana.competition.dao.SettingsDao;
import eu.motogymkhana.competition.model.Country;
import eu.motogymkhana.competition.model.Credential;
import eu.motogymkhana.competition.settings.Settings;

/**
 * Created by christine on 14-2-16.
 */
public class SettingsDaoImpl extends BaseDaoImpl<Settings, Integer> implements SettingsDao {

    public SettingsDaoImpl(ConnectionSource connectionSource, Class<Settings> dataClass) throws SQLException {
        super(connectionSource, Settings.class);
    }

    @Override
    public Settings get() throws SQLException {

        List<Settings> list = null;
        QueryBuilder<Settings, Integer> statementBuilder = queryBuilder();

        statementBuilder.where()
                .eq(Settings.COUNTRY, Constants.country).and()
                .eq(Settings.SEASON, Constants.season);

        list = query(statementBuilder.prepare());

        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {

            Settings settings = new Settings();
            settings.setCountry(Constants.country);
            settings.setSeason(Constants.season);
            create(settings);

            return settings;
        }
    }

    @Override
    public void store(Settings settings) throws SQLException {

        Settings existingSettings = get();
        if (existingSettings != null) {
            settings.set_id(existingSettings.get_id());
            settings.setHasRounds(existingSettings.hasRounds());
        }

        createOrUpdate(settings);
    }

    @Override
    public void storeCountryAndSeason(Country country, int season) throws SQLException {

        Settings settings = get();

        if (settings == null) {
            settings = new Settings();
            settings.setCountry(country);
            settings.setSeason(season);
            create(settings);
        }
    }

    @Override
    public void storeHasRounds(boolean hasRounds) throws SQLException {

        Settings existingSettings = get();

        if (existingSettings == null) {
            existingSettings = new Settings();
            existingSettings.setCountry(Constants.country);
            existingSettings.setSeason(Constants.season);
        }
        existingSettings.setHasRounds(hasRounds);
        createOrUpdate(existingSettings);
    }
}