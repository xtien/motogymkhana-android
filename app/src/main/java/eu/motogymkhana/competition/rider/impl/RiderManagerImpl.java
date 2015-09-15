package eu.motogymkhana.competition.rider.impl;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.google.inject.Inject;
import com.google.inject.Singleton;

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

import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.R;
import eu.motogymkhana.competition.adapter.ChangeListener;
import eu.motogymkhana.competition.adapter.TotalsListAdapter;
import eu.motogymkhana.competition.api.ApiManager;
import eu.motogymkhana.competition.api.GetRidersFromServerTask;
import eu.motogymkhana.competition.api.impl.RidersCallback;
import eu.motogymkhana.competition.dao.RiderDao;
import eu.motogymkhana.competition.dao.TimesDao;
import eu.motogymkhana.competition.db.GymkhanaDatabaseHelper;
import eu.motogymkhana.competition.model.Bib;
import eu.motogymkhana.competition.model.Rider;
import eu.motogymkhana.competition.model.Times;
import eu.motogymkhana.competition.prefs.ChristinePreferences;
import eu.motogymkhana.competition.rider.DeleteRiderTask;
import eu.motogymkhana.competition.rider.GetRidersCallback;
import eu.motogymkhana.competition.rider.GetRidersFromDBTask;
import eu.motogymkhana.competition.rider.RiderManager;
import eu.motogymkhana.competition.rider.SendTextTask;
import eu.motogymkhana.competition.rider.SetRegisteredTask;
import eu.motogymkhana.competition.rider.StoreWittyRidersTask;
import eu.motogymkhana.competition.rider.UpdateRiderCallback;
import eu.motogymkhana.competition.rider.UpdateRiderTask;
import eu.motogymkhana.competition.rider.UpdateRidersTask;
import eu.motogymkhana.competition.rider.UploadRidersTask;
import eu.motogymkhana.competition.round.RoundManager;
import roboguice.inject.InjectResource;

@Singleton
public class RiderManagerImpl implements RiderManager {

    private final Context context;
    private ApiManager api;
    private RiderDao riderDao;
    private TimesDao timesDao;
    private GymkhanaDatabaseHelper databaseHelper;

    @InjectResource(R.array.qualification_points)
    private int[] points;

    private ChristinePreferences prefs;

    final private List<ChangeListener> riderResultListeners = new ArrayList<ChangeListener>();

    private String messageText = "";

    private RoundManager roundManager;

    @Inject
    public RiderManagerImpl(RiderDao riderDao, Context context, TimesDao timesDao, ChristinePreferences prefs,
                            GymkhanaDatabaseHelper databaseHelper, ApiManager api, RoundManager roundManager) {

        this.riderDao = riderDao;
        this.timesDao = timesDao;
        this.prefs = prefs;
        this.databaseHelper = databaseHelper;
        this.api = api;
        this.roundManager = roundManager;
        this.context = context;
    }

    @Override
    public void registerRiderResultListener(ChangeListener listener) {
        riderResultListeners.add(listener);
    }

    @Override
    public void unRegisterRiderResultListener(ChangeListener listener) {

        if (riderResultListeners.contains(listener)) {
            riderResultListeners.remove(listener);
        }
    }

    private boolean ridersFileExists() {
        return new File(getRiderFileName()).exists();
    }

    private String getRiderFileName() {
        return Environment.getExternalStorageDirectory().getAbsolutePath().toString() + "/riders.csv";
    }

    private void readRidersFile() throws IOException {

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

                Times times = new Times(roundManager.getDate());
                times.setRider(rider);
                rider.addTimes(times);

                try {
                    rider = store(rider);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Rider store(Rider rider) throws SQLException {

        rider = riderDao.store(rider);
        timesDao.storeRiderTimes(rider);

        return rider;
    }

    @Override
    public List<Rider> getRiders(long date) {

        try {
            return timesDao.getRiders(date);
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

        try {
            callback.onSuccess(timesDao.getRegisteredRiders(roundManager.getDate()));
        } catch (SQLException e) {
            e.printStackTrace();
            callback.onError(e.getMessage());
        }
    }

    @Override
    public void setRegistered(Rider rider, boolean registered) throws SQLException {

        new SetRegisteredTask(context, rider, registered).execute();
    }

    @Override
    public void generateStartNumbers() throws SQLException {

        for (Bib bib : Bib.values()) {
            generateStartNumbers(bib);
        }

    }

    private void generateStartNumbers(Bib bib) throws SQLException {

        long date = roundManager.getDate();

        String dateString = Constants.dateFormat.format(date);

        int start = bib == Bib.G ? 101 : 1;

        List<Rider> registeredRiders = timesDao.getRegisteredRiders(roundManager.getDate(), bib);

        List<Integer> startNumbers = new ArrayList<Integer>();
        for (int i = 0; i < registeredRiders.size(); i++) {
            startNumbers.add(i + start);
        }

        Iterator<Rider> iterator = registeredRiders.iterator();

        Rider prePreviousRider = null;
        Rider previousRider = null;

        while (iterator.hasNext()) {
            Rider rider = iterator.next();
            Times times = rider.getEUTimes(date);
            int startNumber = (int) (Math.random() * startNumbers.size());
            times.setStartNumber(startNumbers.remove(startNumber));
            timesDao.store(times);

            if (rider.getSharing() != 0) {
                if (previousRider != null) {
                    if (previousRider.getRiderNumber() == rider.getSharing()) {
                        if (prePreviousRider != null) {
                            swapStartNumbers(previousRider, prePreviousRider);
                        }
                    }
                }
            }
        }

        new UpdateRidersTask(context, registeredRiders, new UpdateRiderCallback() {

            @Override
            public void onSuccess() {
                notifyDataChanged();
            }

            @Override
            public void onError(String error) {

            }
        }).execute();
    }

    private void swapStartNumbers(Rider previousRider, Rider prePreviousRider) throws SQLException {

        Log.d("xtien", "swap riders " + previousRider.getFirstName() + " " + previousRider.getLastName() + " and " +
                prePreviousRider.getFirstName() + " " + prePreviousRider.getLastName());

        long date = roundManager.getDate();

        int startNumber = prePreviousRider.getStartNumber();
        prePreviousRider.getEUTimes(date).setStartNumber(previousRider.getStartNumber());
        previousRider.getEUTimes(date).setStartNumber(startNumber);
        timesDao.store(prePreviousRider.getEUTimes(date));
        timesDao.store(previousRider.getEUTimes(date));
    }

    @Override
    public Rider getRiderByNumber(int riderNumber) throws SQLException {
        return riderDao.getRiderByNumber(riderNumber);
    }

    @Override
    public void update(Rider rider, final UpdateRiderCallback callback) {

        new UpdateRiderTask(context, rider, new UpdateRiderCallback() {

            @Override
            public void onSuccess() {
                callback.onSuccess();
                notifyDataChanged();
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
                notifyDataChanged();
            }
        }).execute();
    }

    @Override
    public void update(Rider rider) throws SQLException, IOException {

        riderDao.store(rider);

        api.updateRider(rider);
    }

    @Override
    public void getTotals(TotalsListAdapter totalsListAdapter) throws SQLException {

        HashMap<Long, List<Times>> timesMap = new HashMap<Long, List<Times>>();

        List<Rider> riders = riderDao.queryForAllNonDayRider();

        Iterator<Rider> riderIterator = riders.iterator();

        while (riderIterator.hasNext()) {

            Rider rider = riderIterator.next();

            if (rider.hasTimes()) {

                Iterator<Times> timesIterator = rider.getTimes().iterator();

                while (timesIterator.hasNext()) {
                    Times times = timesIterator.next();

                    if (timesMap.get(times.getDate()) == null) {
                        timesMap.put(times.getDate(), new ArrayList<Times>());
                    }

                    timesMap.get(times.getDate()).add(times);
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
                return rhs.getTotalPoints() - lhs.getTotalPoints();
            }

        });

        totalsListAdapter.setRiders(riders);

    }

    @Override
    public void createOrUpdate(Rider rider, final UpdateRiderCallback callback) throws SQLException {

        update(rider, callback);
        notifyDataChanged();
    }

    @Override
    public void loadRidersFile() throws IOException, SQLException {

        if (ridersFileExists()) {

            prefs.setBlockDownload(true);
            riderDao.clear();
            timesDao.clear();
            readRidersFile();

            notifyDataChanged();

        }
    }

    @Override
    public void downloadRiders() throws IOException {
        prefs.setBlockDownload(false);

        loadRidersFromServer();
    }

    @Override
    public void uploadRiders() throws IOException, SQLException {

        List<Rider> riders = riderDao.getRiders();

        new UploadRidersTask(context, riders, new UpdateRiderCallback() {

            @Override
            public void onSuccess() {
            }

            @Override
            public void onError(String error) {
            }
        }).execute();
    }

    @Override
    public Rider getRider(int riderNumber) throws SQLException {
        return riderDao.getRiderByNumber(riderNumber);
    }

    @Override
    public void loadRidersFromServer() {

        new GetRidersFromServerTask(context, new RidersCallback() {

            @Override
            public void onSuccess() {
                notifyDataChanged();
            }

            @Override
            public void onError() {

            }

        }).execute();
    }

    @Override
    public void deleteRider(Rider rider, final RidersCallback callback) {

        new DeleteRiderTask(context, rider, new RidersCallback() {

            @Override
            public void onSuccess() {
                notifyDataChanged();
                callback.onSuccess();
            }

            @Override
            public void onError() {
                notifyDataChanged();
                callback.onError();
            }
        }).execute();
    }

    @Override
    public void sendText(String text) {

        new SendTextTask(context, text).execute();
    }

    @Override
    public String getMessageText() {

        if (messageText != null) {
            return messageText;
        } else {
            return "";
        }
    }

    @Override
    public void setMessageText(String text) {

        if (text != null) {
            this.messageText = text;
        }
    }

    @Override
    public void saveWittyFile() {
        new StoreWittyRidersTask(context).execute();
    }

    @Override
    public void notifyDataChanged() {

        for (ChangeListener listener : riderResultListeners) {
            listener.notifyDataChanged();
        }
    }
}
