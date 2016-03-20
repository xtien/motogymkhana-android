package eu.motogymkhana.competition.api;

import android.content.Context;

import com.google.inject.Inject;

import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.api.impl.RidersCallback;
import eu.motogymkhana.competition.dao.RiderDao;
import eu.motogymkhana.competition.dao.SettingsDao;
import eu.motogymkhana.competition.rider.RiderManager;
import eu.motogymkhana.competition.settings.Settings;
import roboguice.util.RoboAsyncTask;

public class GetRidersFromServerTask extends RoboAsyncTask<Void> {

    @Inject
    private ApiManager apiManager;

    @Inject
    private RiderDao riderDao;

    @Inject
    private SettingsDao settingsDao;

    @Inject
    private RiderManager riderManager;

    private RidersCallback callback;

    public GetRidersFromServerTask(Context context, RidersCallback callback) {
        super(context);
        this.callback = callback;
    }

    @Override
    public Void call() throws Exception {

        Settings settings = settingsDao.get();

        if (settings.hasRounds()) {
            ListRidersResult result = apiManager.getRiders();

            if (result != null && result.getRiders() != null) {
                riderDao.store(result.getRiders(), Constants.country, Constants.season);
            }

            if (result != null && result.getSettings() != null) {
                settingsDao.store(result.getSettings());
            }

            if (result != null && result.getText() != null) {
                riderManager.setMessageText(result.getText());
            }
        }

        return null;
    }

    @Override
    public void onSuccess(Void v) {
        callback.onSuccess();
    }

}
