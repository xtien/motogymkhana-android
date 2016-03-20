package eu.motogymkhana.competition.round.impl;

import android.content.Context;
import android.util.Log;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import eu.motogymkhana.competition.Constants;
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
import eu.motogymkhana.competition.settings.Settings;

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
    public long getDate() {

        long date = 0l;

        try {
            Round round = getRound();
            if (round != null) {
                date = round.getDate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return date;
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

        if (rounds.size() < 1) {
            Settings settings = settingsDao.get();
            if (settings.hasRounds()) {
                loadRoundsFromServer();
            }
        }
        return rounds;
    }

    @Override
    public void loadRoundsFromServer() {

        new GetRoundsTask(context, new RidersCallback() {

            @Override
            public void onSuccess() {
                riderManager.notifyDataChanged();
            }

            @Override
            public void onError() {

            }

        }).execute();
    }

    @Override
    public void setRound(Round r) throws SQLException {

        long date = r.getDate();

        prefs.setDate(date);

        for (Round round : getRounds()) {
            if (round.isCurrent() || date == round.getDate()) {
                round.setCurrent(date == round.getDate());
                roundDao.store(round);
            }
        }
        uploadRounds();
    }

    @Override
    public Round getRound() throws SQLException {
        return roundDao.getCurrentRound();
    }

    @Override
    public String getDateString() {
        return Constants.dateFormat.format(getDate());
    }

    @Override
    public Integer getRoundNumber() throws SQLException {

        Round round = getRound();
        return round == null ? null : round.getNumber();
    }

    @Override
    public void save(List<Round> rounds) throws SQLException {

        List<Round> toDelete = new ArrayList<Round>();
        List<Round> toAdd = new ArrayList<Round>();
        List<Round> existingRounds = getRounds();

        Iterator<Round> iterator = existingRounds.iterator();
        while (iterator.hasNext()) {
            Round round = iterator.next();
            if (!rounds.contains(round)) {
                toDelete.add(round);
            }
        }

        Iterator<Round> iter = rounds.iterator();
        while (iter.hasNext()) {
            Round round = iter.next();
            if (!existingRounds.contains(round)) {
                toAdd.add(round);
            }
        }

        for (Round round : toDelete) {
            roundDao.delete(round);
        }

        for (Round round : toAdd) {
            roundDao.create(round);
        }
    }
}
