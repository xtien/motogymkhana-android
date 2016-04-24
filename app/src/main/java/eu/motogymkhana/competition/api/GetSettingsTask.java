package eu.motogymkhana.competition.api;

import android.content.Context;
import android.os.AsyncTask;

import com.google.inject.Inject;

import java.io.IOException;
import java.sql.SQLException;

import eu.motogymkhana.competition.api.impl.RidersCallback;
import eu.motogymkhana.competition.dao.RiderDao;
import eu.motogymkhana.competition.dao.RoundDao;
import eu.motogymkhana.competition.dao.SettingsDao;
import eu.motogymkhana.competition.rider.RiderManager;
import eu.motogymkhana.competition.settings.SettingsManager;
import roboguice.RoboGuice;

public class GetSettingsTask extends AsyncTask<Void,Void,Void> {

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
        RoboGuice.getInjector(context).injectMembers(this);
        this.callback = callback;
    }

    @Override
    public Void doInBackground(Void... params)  {
        try {
            settingsManager.getSettingsFromServer();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onPostExecute(Void v) {
        callback.onSuccess();
    }

}
