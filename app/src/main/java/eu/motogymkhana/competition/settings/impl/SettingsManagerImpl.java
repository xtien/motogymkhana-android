package eu.motogymkhana.competition.settings.impl;

import android.content.Context;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.io.IOException;
import java.sql.SQLException;

import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.api.ApiManager;
import eu.motogymkhana.competition.api.GetSettingsTask;
import eu.motogymkhana.competition.api.SettingsResult;
import eu.motogymkhana.competition.api.impl.RidersCallback;
import eu.motogymkhana.competition.dao.SettingsDao;
import eu.motogymkhana.competition.rider.RiderManager;
import eu.motogymkhana.competition.rider.UpdateRiderCallback;
import eu.motogymkhana.competition.settings.Settings;
import eu.motogymkhana.competition.settings.SettingsManager;
import eu.motogymkhana.competition.settings.UploadSettingsTask;

/**
 * Created by christine on 21-2-16.
 */
@Singleton
public class SettingsManagerImpl implements SettingsManager {

    @Inject
    private Context context;

    @Inject
    private RiderManager riderManager;

    @Inject
    private ApiManager apiManager;

    @Inject
    private SettingsDao settingsDao;

    @Override
    public void getSettingsFromServerAsync() throws IOException, SQLException {

        new GetSettingsTask(context, new RidersCallback() {

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
    public Settings getSettingsFromServer() throws IOException, SQLException {

        SettingsResult result = apiManager.getSettings();
        Settings settings = null;

        if (result != null) {

            settings = result.getSettings();

            if (result.getSettings() == null) {
                settings = new Settings();
                settings.setCountry(Constants.country);
                settings.setSeason(Constants.season);
            }
            settingsDao.store(settings);
        }

        return settings;
    }

    @Override
    public void uploadSettingsToServer(Settings settings) {

        new UploadSettingsTask(context, settings, new UpdateRiderCallback() {

            @Override
            public void onSuccess() {
                //riderManager.notifyDataChanged();
            }

            @Override
            public void onError(String error) {
            }

        }).execute();

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
    public int getRoundsForBib() {
        Settings settings = null;
        try {
            settings = settingsDao.get();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
        return settings.getRoundsForBib();
    }

    @Override
    public Settings getSettings() throws IOException, SQLException {

        Settings settings = null;

        try {
            settings = settingsDao.get();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (settings == null) {
            getSettingsFromServerAsync();
            settings = new Settings();
            settings.setSeason(Constants.season);
            settings.setCountry(Constants.country);
            try {
                settingsDao.store(settings);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return settings;
    }
}
