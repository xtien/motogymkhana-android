package eu.motogymkhana.competition.round;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import eu.motogymkhana.competition.model.Round;

/**
 * Created by christine on 26-5-15.
 */
public interface RoundManager {

    long getDate();

    Round getDate(int number) throws SQLException;

    void setDate(long date) throws SQLException;

    Round getNextRound() throws ParseException, SQLException;

    void uploadRounds() throws SQLException;

    List<Round> getRounds() throws SQLException;

    void loadRoundsFromServer();

    void setRound(Round round) throws SQLException;

    Round getRound() throws SQLException;

    String getDateString();

    Integer getRoundNumber() throws SQLException;

    void save(List<Round> rounds) throws SQLException;
}
