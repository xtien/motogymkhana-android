package eu.motogymkhana.competition.dao;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import eu.motogymkhana.competition.model.Country;
import eu.motogymkhana.competition.model.Round;

public interface RoundDao extends Dao<Round, Integer> {

	Round store(Round round) throws SQLException;

	Round getRoundByDate(long date) throws SQLException;

	List<Round> getRounds() throws SQLException;

	Round getRoundByNumber(int number) throws SQLException;

	void store(Collection<Round> rounds) throws SQLException;

	Round getCurrentRound() throws SQLException;

	void delete(Country eu, int i) throws SQLException;

	void remove(Round r) throws SQLException;

	long getCurrentDate() throws SQLException;
}
