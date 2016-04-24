package eu.motogymkhana.competition.round;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import eu.motogymkhana.competition.model.Round;

/**
 * Created by christine on 26-5-15.
 */
public interface RoundManager {

    Round getDate(int number) throws SQLException;

    void setDate(long date) throws SQLException;

    Round getNextRound() throws ParseException, SQLException;

    void uploadRounds() throws SQLException;

    List<Round> getRounds() throws SQLException;

    void loadRoundsFromServer();

    Round getCurrentRound() throws SQLException;

    void save(List<Round> rounds) throws SQLException;

    Round getRoundForDate(long date) throws SQLException;

    long getDate();
}
