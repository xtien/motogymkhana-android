package eu.motogymkhana.competition.dao;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import eu.motogymkhana.competition.model.Country;
import eu.motogymkhana.competition.settings.Settings;

/**
 * Created by christine on 14-2-16.
 */
public interface SettingsDao extends Dao<Settings, Integer> {

    Settings get() throws SQLException;

    void store(Settings settings) throws SQLException;

    void storeCountryAndSeason(Country country, int season) throws SQLException;

    void storeHasRounds(boolean hasRounds) throws SQLException;
}
