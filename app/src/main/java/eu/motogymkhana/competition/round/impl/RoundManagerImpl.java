package eu.motogymkhana.competition.round.impl;

import android.content.Context;

import javax.inject.Inject;
import javax.inject.Singleton;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import eu.motogymkhana.competition.api.ApiManager;
import eu.motogymkhana.competition.api.ResponseHandler;
import eu.motogymkhana.competition.api.response.ListRoundsResult;
import eu.motogymkhana.competition.dao.RoundDao;
import eu.motogymkhana.competition.dao.SettingsDao;
import eu.motogymkhana.competition.log.MyLog;
import eu.motogymkhana.competition.model.Round;
import eu.motogymkhana.competition.notify.Notifier;
import eu.motogymkhana.competition.prefs.MyPreferences;
import eu.motogymkhana.competition.rider.RiderManager;
import eu.motogymkhana.competition.round.RoundComparatorByDate;
import eu.motogymkhana.competition.round.RoundManager;

/**
 * Created by christine on 26-5-15.
 */
@Singleton
public class RoundManagerImpl implements RoundManager {

    private static final String LOGTAG = RoundManagerImpl.class.getSimpleName();

    @Inject
    protected MyPreferences prefs;

    @Inject
    protected Context context;

    @Inject
    protected RoundDao roundDao;

    @Inject
    protected RiderManager riderManager;

    @Inject
    protected SettingsDao settingsDao;

    @Inject
    protected ApiManager api;

    @Inject
    protected Notifier notifier;

    @Inject
    protected MyLog log;

    private ResponseHandler getRoundsResponseHandler = new ResponseHandler() {

        @Override
        public void onSuccess(Object object) {

            ListRoundsResult result = (ListRoundsResult) object;
            try {
                roundDao.store(result.getRounds());
                notifier.notifyDataChanged();
            } catch (SQLException e) {
                onException(e);
            }
        }

        @Override
        public void onException(Exception e) {
            log.e(LOGTAG, e);
        }

        @Override
        public void onError(int statusCode, String string) {
            log.e(LOGTAG, "" + statusCode + " " + string);
        }
    };

    @Inject
    public RoundManagerImpl() {

    }

    @Override
    public void setDate(long date) throws SQLException {

        prefs.setDate(date);

        for (Round round : getRounds()) {
            if (round.isCurrent() || date == round.getDate()) {
                round.setCurrent(date == round.getDate());
                roundDao.store(round);
            }
        }

        notifier.notifyDataChanged();
    }

    @Override
    public Round getDate(int roundNumber) throws SQLException {
        return roundDao.getRoundByNumber(roundNumber);
    }

    @Override
    public Round getNextRound() throws ParseException, SQLException {

        int numberOfRounds = getRounds().size();

        long now = System.currentTimeMillis();
        Round round = null;

        for (int i = numberOfRounds; i > 0; i--) {

            round = getDate(i);
            if (round != null && round.getDate() < now - (24l * 3600l * 1000l)) {
                return round;
            }
        }

        return round;
    }

    @Override
    public void uploadRounds(ResponseHandler responseHandler) throws SQLException {

        Collection<Round> rounds = roundDao.getRounds();

        api.uploadRounds(rounds, responseHandler);
    }

    @Override
    public List<Round> getRounds() throws SQLException {

        List<Round> rounds = roundDao.getRounds();
        return rounds;
    }

    @Override
    public void loadRoundsFromServer() {
        api.getRounds(getRoundsResponseHandler);
    }

    @Override
    public Round getCurrentRound() throws SQLException {
        return roundDao.getCurrentRound();
    }

    @Override
    public Round getRoundForDate(long date) throws SQLException {

        for (Round r : roundDao.getRounds()) {
            if (r.getDate() == date) {
                return r;
            }
        }
        return getCurrentRound();
    }

    @Override
    public void save(List<Round> rounds) throws SQLException {

        Collections.sort(rounds, new RoundComparatorByDate());

        int i = 0;
        for (Round r : rounds) {
            System.out.println("round " + r.getDateString());
            r.setNumber(++i);
        }

        List<Round> existingRounds = getRounds();

        for (Round r : rounds) {
            if (existingRounds.contains(r)) {
                existingRounds.remove(r);
            }
            roundDao.store(r);
            System.out.println("round " + r.getNumber() + " " + r.getDateString());
        }

        for (Round r : existingRounds) {
            roundDao.remove(r);
        }
    }
}
