package eu.motogymkhana.competition.rider.impl;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.R;
import eu.motogymkhana.competition.adapter.TotalsListAdapter;
import eu.motogymkhana.competition.api.ApiManager;
import eu.motogymkhana.competition.api.ResponseHandler;
import eu.motogymkhana.competition.api.response.ListRidersResult;
import eu.motogymkhana.competition.dao.RiderDao;
import eu.motogymkhana.competition.dao.TimesDao;
import eu.motogymkhana.competition.db.GymkhanaDatabaseHelper;
import eu.motogymkhana.competition.log.MyLog;
import eu.motogymkhana.competition.model.Bib;
import eu.motogymkhana.competition.model.Country;
import eu.motogymkhana.competition.model.Rider;
import eu.motogymkhana.competition.model.Times;
import eu.motogymkhana.competition.notify.Notifier;
import eu.motogymkhana.competition.prefs.MyPreferences;
import eu.motogymkhana.competition.rider.GetRegisteredRidersFromDBTask;
import eu.motogymkhana.competition.rider.GetRidersCallback;
import eu.motogymkhana.competition.rider.GetRidersFromDBTask;
import eu.motogymkhana.competition.rider.RiderManager;
import eu.motogymkhana.competition.settings.SettingsManager;

@Singleton
public class RiderManagerImpl implements RiderManager {

    private static final String LOGTAG = RiderManagerImpl.class.getSimpleName();

    @Inject
    protected Context context;

    @Inject
    protected SettingsManager settingsManager;

    @Inject
    protected ApiManager api;

    @Inject
    protected RiderDao riderDao;

    @Inject
    protected TimesDao timesDao;

    @Inject
    protected GymkhanaDatabaseHelper databaseHelper;

    @Inject
    protected Notifier notifier;

    @Inject
    protected MyPreferences prefs;

    private List<Integer> riderMap = new ArrayList<Integer>();
    private int[] points;
    private String messageText = "";

    @Inject
    protected MyLog log;

    @Singleton
    @Inject
    public RiderManagerImpl() {
    }

    private boolean ridersFileExists() {
        return new File(getRiderFileName()).exists();
    }

    private String getRiderFileName() {
        return Environment.getExternalStorageDirectory().getAbsolutePath().toString() + "/riders.csv";
    }

    private void readRidersFile() throws IOException {

        Country country = Constants.country;
        int season = Constants.season;

        List<String> riderList = new ArrayList<String>();

        File ridersFile = new File(getRiderFileName());
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(ridersFile)));

        String line = reader.readLine();
        while (line != null) {
            if (line != null && line.length() > 0) {
                riderList.add(line);
            }
            line = reader.readLine();
        }

        for (String riderString : riderList) {
            Rider rider = new Rider(riderString);

            if (rider.isValid()) {

                Times times = new Times(prefs.getDate());
                times.setRider(rider);
                times.setCountry(country);
                times.setSeason(season);
                rider.addTimes(times);

                try {
                    store(rider);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Rider store(Rider rider) throws SQLException {
        rider = riderDao.store(rider);
        return rider;
    }

    @Override
    public List<Rider> getRiders(long date) {

        try {
            List<Rider> riders = timesDao.getRiders(date);
            if (riderMap.size() == 0) {
                for (Rider r : riders) {
                    riderMap.add(r.getRiderNumber());
                }
            }
            return riders;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<Rider>();
        }
    }

    @Override
    public void getRiders(GetRidersCallback callback) {
        new GetRidersFromDBTask(context, callback).execute();
    }

    @Override
    public void getRegisteredRiders(GetRidersCallback callback) {
        new GetRegisteredRidersFromDBTask(context, callback, prefs.getDate()).execute();
    }

    @Override
    public void setRegistered(Rider rider, boolean registered, ResponseHandler responseHandler) throws SQLException {

        Times times = rider.getEUTimes(prefs.getDate());
        if (times == null) {
            times = new Times();
            times.setRider(rider);
            times.setDate(prefs.getDate());
            times.setSeason(Constants.season);
            times.setCountry(Constants.country);
            times.setRiderNumber(rider.getRiderNumber());
            rider.addTimes(times);
        }

        times.setTimeStamp(System.currentTimeMillis());
        times.setRegistered(registered);
        if (!registered) {
            times.clearStartNumber();
        }
        times.setRiderNumber(times.getRider().getRiderNumber());
        update(times, responseHandler);
    }

    @Override
    public void generateStartNumbers() {

        try {
            for (Bib bib : Bib.values()) {
               /*
                all startnumbers have been assigned, or none
                 */
                generateStartNumbers(bib);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void generateStartNumbers(Bib bib) throws SQLException {

        Log.d(LOGTAG, "generate start numbers for " + bib.name());
        long date = prefs.getDate();
        int start = bib == Bib.G ? 101 : (bib == Bib.B ? 201 : (bib == Bib.R ? 301 : 1));

        List<Rider> registeredRiders = timesDao.getRegisteredRiders(date, bib);

        for (Rider rider : registeredRiders) {
            Log.d(LOGTAG, rider.getFullName() + " " + rider.get_id() + " " + rider.getRiderId() + " ");
        }

        Log.d(LOGTAG, "number of registered riders " + registeredRiders.size());

        List<Integer> startNumbers = new ArrayList<Integer>();
        for (int i = 0; i < registeredRiders.size(); i++) {
            startNumbers.add(i + start);
            Log.d(LOGTAG, Integer.toString(i));
        }

        Iterator<Rider> iterator = registeredRiders.iterator();

        while (iterator.hasNext()) {
            Rider rider = iterator.next();
            Times times = rider.getEUTimes(date);
            int randomNumber = (int) (Math.random() * startNumbers.size());
            times.setStartNumber(startNumbers.remove(randomNumber));
            timesDao.store(times);
            Log.d(LOGTAG, rider.getFullName() + " has startnumber " + times.getStartNumber());
        }

        List<Rider> unRegisteredRiders = timesDao.getUnregisteredRiders(date, bib);
        for (Rider rider : unRegisteredRiders) {
            Times times = rider.getEUTimes(date);
            if (times == null) {
                times = new Times(date);
                times.setRider(rider);
                rider.addTimes(times);
            }
        }
    }

    @Override
    public Rider getRiderByNumber(String riderNumber) throws SQLException {
        return riderDao.getRiderByServerId(riderNumber);
    }

    @Override
    public void update(Times times, ResponseHandler responseHandler) {

        try {
            timesDao.store(times);
        } catch (SQLException e) {
            responseHandler.onException(e);
        }

        times.setRiderNumber();
        api.updateRider(times.getRider(), responseHandler);
    }

    @Override
    public void updateTo2016(Rider rider, ResponseHandler responseHandler) {
        rider.setSeason(2016);
        rider.setCountry(Constants.country);
        rider.clearTimes();
        update(rider, responseHandler);
    }

    @Override
    public int newRiderNumber() {

        if (riderMap.size() == 0) {
            try {
                for (Rider r : riderDao.getRiders()) {
                    riderMap.add(r.getRiderNumber());
                }
            } catch (SQLException e) {
                log.e(LOGTAG, e);
            }
        }

        for (int i = 1; i < riderMap.size() + 1; i++) {
            if (!riderMap.contains(i)) {
                riderMap.add(i);
                return i;
            }
        }

        int newNumber = riderMap.size() + 1;
        riderMap.add(newNumber);
        return newNumber;
    }

    @Override
    public void getAllRiders(ResponseHandler responseHandler) {
        api.getAllRiders(responseHandler);
    }

    @Override
    public boolean hasRiders() throws SQLException {
        return riderDao.getRiders().size() > 0;
    }

    @Override
    public void updateToEU(Rider rider, ResponseHandler responseHandler) {
        rider.setCountry(Country.EU);
        rider.setSeason(2016);
        update(rider, responseHandler);
    }

    @Override
    public void update(Rider rider, ResponseHandler responseHandler) {

        try {
            riderDao.store(rider);
        } catch (SQLException e) {
            responseHandler.onException(e);
        }

        api.updateRider(rider, responseHandler);
    }

    @Override
    public void getTotals(TotalsListAdapter totalsListAdapter) throws SQLException, IOException {

        points = settingsManager.getSettings().getPoints();
        if (points == null) {
            points = context.getResources().getIntArray(R.array.qualification_points);
        }

        final int roundsCountingForSeasonResult = settingsManager.getRoundsCountingForSeasonResult();

        HashMap<Long, List<Times>> timesMap = new HashMap<Long, List<Times>>();

        List<Rider> riders = riderDao.queryForAllNonDayRider();

        Iterator<Rider> riderIterator = riders.iterator();

        while (riderIterator.hasNext()) {

            Rider rider = riderIterator.next();

            if (rider.hasTimes()) {

                Iterator<Times> timesIterator = rider.getTimes().iterator();

                while (timesIterator.hasNext()) {
                    Times times = timesIterator.next();
                    if (times.getCountry() == Constants.country) {
                        if (timesMap.get(times.getDate()) == null) {
                            timesMap.put(times.getDate(), new ArrayList<Times>());
                        }

                        timesMap.get(times.getDate()).add(times);
                    }
                }
            }
        }

        for (long key : timesMap.keySet()) {

            List<Times> list = new ArrayList<Times>();
            list.addAll(timesMap.get(key));

            Collections.sort(list, new Comparator<Times>() {

                @Override
                public int compare(Times lhs, Times rhs) {
                    return lhs.getBestTime() - rhs.getBestTime();
                }

            });

            int pointsPointer = 0;
            for (Times times : list) {
                if (times.getBestTime() > 0 && pointsPointer < points.length) {

                    int p = points[pointsPointer++];

                    times.setPoints(p);
                 }
            }
        }
        Collections.sort(riders, new Comparator<Rider>() {

            @Override
            public int compare(Rider lhs, Rider rhs) {
                return rhs.getTotalPoints(roundsCountingForSeasonResult) - lhs.getTotalPoints(roundsCountingForSeasonResult);
            }

        });

        totalsListAdapter.setRiders(riders);

    }

    @Override
    public void loadRidersFile() throws IOException, SQLException {

        if (ridersFileExists()) {

            prefs.setBlockDownload(true);
            riderDao.clear();
            timesDao.clear();
            readRidersFile();

            notifier.notifyDataChanged();
        }
    }

    @Override
    public void downloadRiders(ResponseHandler responseHandler) throws IOException {
        prefs.setBlockDownload(false);

        loadRidersFromServer(responseHandler);
    }

    @Override
    public void uploadRiders(ResponseHandler responseHandler) {

        try {
            List<Rider> riders = riderDao.getRiders();
            api.uploadRiders(riders, responseHandler);
        } catch (SQLException e) {
            responseHandler.onException(e);
        }
    }

    @Override
    public Rider getRiderByServerId(String serverRiderId) throws SQLException {
        return riderDao.getRiderByServerId(serverRiderId);
    }

    @Override
    public void loadRidersFromServer(final ResponseHandler responseHandler) {

        api.getRiders(new ResponseHandler() {

            @Override
            public void onSuccess(Object object) {
                //System.out.println("onSuccess 1");

                ListRidersResult result = (ListRidersResult) object;

                if (result.getRiders() != null) {
                    try {
                        riderDao.store(result.getRiders());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    for (Rider r : result.getRiders()) {
                        riderMap.add(r.getRiderNumber());
                    }
                }
                // System.out.println("onSuccess 2");
                responseHandler.onSuccess(object);
            }

            @Override
            public void onException(Exception e) {
                responseHandler.onException(e);
            }

            @Override
            public void onError(int statusCode, String string) {
                responseHandler.onError(statusCode, string);
            }
        });
    }

    @Override
    public void deleteRider(Rider rider, final ResponseHandler responseHandler) {

        try {
            riderDao.delete(rider);
        } catch (SQLException e) {
            responseHandler.onException(e);
        }

        api.delete(rider, responseHandler);
    }

    @Override
    public void sendText(String text, ResponseHandler responseHandler) {
        api.sendText(text, responseHandler);
    }

    @Override
    public void setMessageText(String text) {

        if (text != null) {
            this.messageText = text;
        }
    }

    @Override
    public void saveWittyFile() {
    }
}
