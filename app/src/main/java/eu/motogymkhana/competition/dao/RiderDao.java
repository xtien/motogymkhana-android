package eu.motogymkhana.competition.dao;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import eu.motogymkhana.competition.model.Country;
import eu.motogymkhana.competition.model.Rider;

public interface RiderDao extends Dao<Rider, Integer> {

    Rider store(Rider rider) throws SQLException;

    Rider getRiderByServerId(String id) throws SQLException;

    Rider getRiderById(int id) throws SQLException;

    List<Rider> queryForAllNonDayRider() throws SQLException;

    List<Rider> getRiders() throws SQLException;

    List<Rider> getAllRiders() throws SQLException;

    void store(Collection<Rider> riders) throws SQLException;

    void deleteRider(Rider rider) throws SQLException;

    void clear() throws SQLException;
}
