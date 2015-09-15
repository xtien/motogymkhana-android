package eu.motogymkhana.competition.round.impl;

import android.content.Context;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.api.GetRoundsTask;
import eu.motogymkhana.competition.api.impl.RidersCallback;
import eu.motogymkhana.competition.dao.RoundDao;
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

    private final ChristinePreferences prefs;
    private final Context context;
    private RoundDao roundDao;
    private RiderManager riderManager;

    @Inject
    public RoundManagerImpl(Context context, ChristinePreferences prefs, RoundDao roundDao, RiderManager riderManager) {

        this.prefs = prefs;
        this.context = context;
        this.roundDao = roundDao;
        this.riderManager = riderManager;
    }

    @Override
    public long getDate() {

        long date = prefs.getDate();

        if (date == 0l) {

            try {
                Round round = getNextRound();
                if (round != null) {
                    date = round.getDate();
                }

            } catch (ParseException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            prefs.setDate(date);
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
    public void loadDates() throws SQLException, ParseException {

        Collection<Round> rounds = roundDao.getRounds();

        if (rounds == null || rounds.size() == 0) {

            for (int i = 1; i < 9; i++) {
                String dateString = context.getString(context.getResources().getIdentifier("date" + i, "string",
                        context.getPackageName()));

                long date = Constants.dateFormat.parse(dateString).getTime();

                Round round = new Round(i, date);

                roundDao.store(round);

                if (rounds == null) {
                    rounds = new ArrayList<Round>();
                }

                rounds.add(round);
            }
        }

        new UploadRoundsTask(context, rounds, null).execute();
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
        return roundDao.getRounds();
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
    public String getDateString(){
        return Constants.dateFormat.format(getDate());
    }
}
