package eu.motogymkhana.competition.round.impl;

import android.content.Context;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import eu.motogymkhana.competition.api.GetRoundsTask;
import eu.motogymkhana.competition.api.impl.RidersCallback;
import eu.motogymkhana.competition.dao.RoundDao;
import eu.motogymkhana.competition.dao.SettingsDao;
import eu.motogymkhana.competition.model.Round;
import eu.motogymkhana.competition.prefs.ChristinePreferences;
import eu.motogymkhana.competition.rider.RiderManager;
import eu.motogymkhana.competition.rider.UpdateRiderCallback;
import eu.motogymkhana.competition.round.RoundManager;
import eu.motogymkhana.competition.round.UploadRoundsTask;

/**
 * Created by christine on 26-5-15.
 */
@Singleton
public class RoundManagerImpl implements RoundManager {

    private static final String LOGTAG = RoundManagerImpl.class.getSimpleName();

    private final ChristinePreferences prefs;
    private final Context context;
    private RoundDao roundDao;
    private RiderManager riderManager;
    private SettingsDao settingsDao;

    @Inject
    public RoundManagerImpl(Context context, ChristinePreferences prefs, RoundDao roundDao, RiderManager
            riderManager, SettingsDao settingsDao) {

        this.prefs = prefs;
        this.context = context;
        this.roundDao = roundDao;
        this.riderManager = riderManager;
        this.settingsDao = settingsDao;
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

        riderManager.notifyDataChanged();
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
    public void uploadRounds() throws SQLException {

        Collection<Round> rounds = roundDao.getRounds();

        new UploadRoundsTask(context, rounds, new UpdateRiderCallback() {

            @Override
            public void onSuccess() {
                riderManager.notifyDataChanged();
            }

            @Override
            public void onError(String error) {
            }

        }).execute();
    }

    @Override
    public List<Round> getRounds() throws SQLException {

        List<Round> rounds = roundDao.getRounds();
        return rounds;
    }

    @Override
    public void loadRoundsFromServer() {

        new GetRoundsTask(context, new RidersCallback() {

            @Override
            public void onSuccess() {
                //riderManager.notifyDataChanged();
            }

            @Override
            public void onError() {

            }

        }).execute();
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
    public long getDate() {
        return prefs.getDate();
    }

    @Override
    public void save(List<Round> rounds) throws SQLException {

        Collections.sort(rounds, new Comparator<Round>() {

            @Override
            public int compare(Round lhs, Round rhs) {
                return (int) (lhs.getDate() - rhs.getDate());
            }
        });

        int i = 0;
        for (Round r : rounds) {
            r.setNumber(++i);
        }

        List<Round> existingRounds = getRounds();

        for (Round r : rounds) {
            if (existingRounds.contains(r)) {
                existingRounds.remove(r);
            }
            roundDao.store(r);
        }

        for (Round r : existingRounds) {
            roundDao.remove(r);
        }
    }
}
