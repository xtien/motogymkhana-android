package eu.motogymkhana.competition.api;

import android.content.Context;
import android.os.AsyncTask;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.inject.Inject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.api.impl.RidersCallback;
import eu.motogymkhana.competition.dao.RiderDao;
import eu.motogymkhana.competition.dao.RoundDao;
import eu.motogymkhana.competition.dao.SettingsDao;
import eu.motogymkhana.competition.model.Round;
import eu.motogymkhana.competition.rider.RiderManager;
import roboguice.RoboGuice;

public class GetRidersFromServerTask extends AsyncTask<Void, Void, Void> {

    @Inject
    private ApiManager apiManager;

    @Inject
    private RiderDao riderDao;

    @Inject
    private RoundDao roundsDao;

    @Inject
    private SettingsDao settingsDao;

    @Inject
    private RiderManager riderManager;

    private RidersCallback callback;

    public GetRidersFromServerTask(Context context, RidersCallback callback) {
        RoboGuice.getInjector(context).injectMembers(this);
        this.callback = callback;
    }

    @Override
    public Void doInBackground(Void... params) {

        ListRidersResult result = null;

        try {
            List<Round> rounds = roundsDao.getRounds();

            if (rounds != null && rounds.size() > 0) {
                result = apiManager.getRiders();
            }
            if (result != null && result.getRiders() != null) {
                riderDao.store(result.getRiders(), Constants.country, Constants.season);
            }

            if (result != null && result.getSettings() != null) {
                settingsDao.store(result.getSettings());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (result != null && result.getText() != null) {
            riderManager.setMessageText(result.getText());
        }

        return null;
    }

    @Override
    public void onPostExecute(Void v) {
        callback.onSuccess();
    }

}
