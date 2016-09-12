package eu.motogymkhana.competition.dao;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import eu.motogymkhana.competition.model.Bib;
import eu.motogymkhana.competition.model.Country;
import eu.motogymkhana.competition.model.Rider;
import eu.motogymkhana.competition.model.Times;

public interface TimesDao extends Dao<Times, Integer> {

    void storeRiderTimes(Rider rider) throws SQLException;

    Times getTimes(int riderId, long date) throws SQLException;

    List<Rider> getRegisteredRiders(long date) throws SQLException;

    List<Times> getTimes(long date) throws SQLException;

    List<Times> getTimesSortedOnStartNumber(long date, Bib bib) throws SQLException;

    List<Rider> getRiders(long date) throws SQLException;

    void store(Times times) throws SQLException;

    void updateStartNumber(Times times) throws SQLException;

    void clear() throws SQLException;

    List<Rider> getUnregisteredRiders(long date, Bib bib) throws SQLException;

    List<Rider> getRegisteredRiders(long date, Bib bib) throws SQLException;

    void delete(Country country, int season) throws SQLException;
}
