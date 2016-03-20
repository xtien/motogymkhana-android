package eu.motogymkhana.competition.api;

import android.content.Context;

import com.google.inject.Inject;

import java.util.List;

import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.api.impl.RidersCallback;
import eu.motogymkhana.competition.dao.RiderDao;
import eu.motogymkhana.competition.dao.RoundDao;
import eu.motogymkhana.competition.dao.SettingsDao;
import eu.motogymkhana.competition.model.Round;
import eu.motogymkhana.competition.rider.RiderManager;
import eu.motogymkhana.competition.settings.Settings;
import eu.motogymkhana.competition.settings.SettingsManager;
import roboguice.util.RoboAsyncTask;

public class GetSettingsTask extends RoboAsyncTask<Void> {

    @Inject
    private ApiManager apiManager;

    @Inject
    private SettingsDao settingsDao;

    @Inject
    private RiderManager riderManager;

    @Inject
    private SettingsManager settingsManager;

    @Inject
    private RoundDao roundDao;

    @Inject
    private RiderDao riderDao;

    private RidersCallback callback;

    public GetSettingsTask(Context context, RidersCallback callback) {
        super(context);
        this.callback = callback;
    }

    @Override
    public Void call() throws Exception {
        settingsManager.getSettingsFromServer();
        return null;
    }

    @Override
    public void onSuccess(Void v) {
        callback.onSuccess();
    }

}
