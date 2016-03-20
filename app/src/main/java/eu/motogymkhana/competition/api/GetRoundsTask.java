package eu.motogymkhana.competition.api;

import android.content.Context;

import com.google.inject.Inject;

import java.util.List;

import eu.motogymkhana.competition.api.impl.RidersCallback;
import eu.motogymkhana.competition.dao.RoundDao;
import eu.motogymkhana.competition.dao.SettingsDao;
import eu.motogymkhana.competition.model.Round;
import eu.motogymkhana.competition.rider.RiderManager;
import eu.motogymkhana.competition.round.RoundManager;
import eu.motogymkhana.competition.settings.Settings;
import eu.motogymkhana.competition.settings.SettingsManager;
import roboguice.util.RoboAsyncTask;

public class GetRoundsTask extends RoboAsyncTask<Void> {

    @Inject
    private ApiManager apiManager;

    @Inject
    private RoundDao roundDao;

    @Inject
    private RoundManager roundManager;

    @Inject
    private RiderManager riderManager;

    @Inject
    private SettingsDao settingsDao;

    private RidersCallback callback;

    @Inject
    private SettingsManager settingsManager;

    public GetRoundsTask(Context context, RidersCallback callback) {
        super(context);
        this.callback = callback;
    }

    @Override
    public Void call() throws Exception {

        Settings settings = settingsDao.get();

        settings = settingsManager.getSettingsFromServer();

        if (settings.hasRounds()) {

            ListRoundsResult result = apiManager.getRounds();

            if (result != null && result.getRounds() != null && result.getRounds().size() > 0) {
                roundDao.store(result.getRounds());
                roundManager.getRounds();
            } else {
                settingsDao.storeHasRounds(false);
            }

            riderManager.loadRidersFromServer();
        }

        return null;
    }

    @Override
    public void onSuccess(Void v) {
        callback.onSuccess();
    }

}
