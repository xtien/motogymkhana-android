package eu.motogymkhana.competition.settings.impl;

import android.content.Context;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import javax.inject.Inject;
import javax.inject.Singleton;

import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.api.ApiManager;
import eu.motogymkhana.competition.api.ResponseHandler;
import eu.motogymkhana.competition.api.response.SettingsResult;
import eu.motogymkhana.competition.dao.SettingsDao;
import eu.motogymkhana.competition.log.MyLog;
import eu.motogymkhana.competition.model.Country;
import eu.motogymkhana.competition.model.Round;
import eu.motogymkhana.competition.rider.RiderManager;
import eu.motogymkhana.competition.settings.Settings;
import eu.motogymkhana.competition.settings.SettingsManager;
import toothpick.Lazy;

/**
 * Created by christine on 21-2-16.
 */
@Singleton
public class SettingsManagerImpl implements SettingsManager {

    private static final String LOGTAG = SettingsManagerImpl.class.getSimpleName();
    @Inject
    protected Context context;

    @Inject
    protected Lazy<RiderManager> riderManager;

    @Inject
    protected Lazy<ApiManager> apiManager;

    @Inject
    protected SettingsDao settingsDao;

    ScheduledExecutorService es = new ScheduledThreadPoolExecutor(1);

    @Inject
    protected MyLog log;

    private ResponseHandler getSettingsFromServerResponseHandler = new ResponseHandler() {

        @Override
        public void onSuccess(Object object) {
             try {
                SettingsResult result = (SettingsResult) object;

                settingsDao.store(result.getSettings());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onException(Exception e) {
            log.e(LOGTAG, e);
        }

        @Override
        public void onError(int statusCode, String string) {
            log.e(LOGTAG, "getSettingsFromServerResponseHandler " + statusCode + " " + string);
        }
    };

    private ResponseHandler uploadSettingsResponseHandler = new ResponseHandler() {

        @Override
        public void onSuccess(Object object) {

        }

        @Override
        public void onException(Exception e) {
            log.e(LOGTAG, e);
        }

        @Override
        public void onError(int statusCode, String string) {
            log.e(LOGTAG, "uploadSettingsResponseHandler " + statusCode + " " + string);
        }
    };

    public SettingsManagerImpl() {

        es.submit(new Runnable() {

            @Override
            public void run() {
                try {
                    if (settingsDao.get() == null) {
                        getSettings(getSettingsFromServerResponseHandler);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 1000);
    }

    @Override
    public void getSettingsFromServer(ResponseHandler responseHandler) {
        apiManager.get().getSettings(responseHandler);
    }

    @Override
    public void setSettings(Settings settings) {

        if (settings == null) {
            settings = new Settings();
            settings.setCountry(Constants.country);
            settings.setSeason(Constants.season);
        }
        try {
            settingsDao.store(settings);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void store(Settings settings) throws SQLException {
        settingsDao.store(settings);
    }

    @Override
    public void storeCountryAndSeason(Country country, int season) throws SQLException {
        settingsDao.storeCountryAndSeason(country, season);
    }

    @Override
    public Settings getSettings() throws SQLException {
        return settingsDao.get();
    }

    @Override
    public void uploadSettingsToServer(Settings settings, ResponseHandler responseHandler) {
        apiManager.get().uploadSettings(settings, responseHandler);
    }

    @Override
    public int getRoundsCountingForSeasonResult() {
        Settings settings = null;
        try {
            settings = settingsDao.get();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
        return settings != null ? settings.getRoundsForSeasonResult() : 0;
    }

    @Override
    public Settings getSettings(ResponseHandler responseHandler) throws IOException, SQLException {

        getSettingsFromServer(responseHandler);

        Settings settings = null;

        try {
            settings = settingsDao.get();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return settings;
    }

    @Override
    public void setRounds(List<Round> rounds) throws IOException, SQLException {

        Settings settings = getSettings(getSettingsFromServerResponseHandler);
        settings.setHasRounds(rounds != null && rounds.size() > 0);
        settingsDao.storeHasRounds(settings.hasRounds());
        uploadSettingsToServer(settings, uploadSettingsResponseHandler);
    }
}
