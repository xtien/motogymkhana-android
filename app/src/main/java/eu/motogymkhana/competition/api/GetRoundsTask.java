package eu.motogymkhana.competition.api;

import android.content.Context;
import android.os.AsyncTask;

import com.google.inject.Inject;

import java.io.IOException;
import java.sql.SQLException;

import eu.motogymkhana.competition.api.impl.RidersCallback;
import eu.motogymkhana.competition.dao.RoundDao;
import eu.motogymkhana.competition.rider.RiderManager;
import eu.motogymkhana.competition.round.RoundManager;
import roboguice.RoboGuice;

public class GetRoundsTask extends AsyncTask<Void,Void,Void> {

    @Inject
    private ApiManager apiManager;

    @Inject
    private RoundDao roundDao;

    @Inject
    private RoundManager roundManager;

    @Inject
    private RiderManager riderManager;

    private RidersCallback callback;

    public GetRoundsTask(Context context, RidersCallback callback) {
        RoboGuice.getInjector(context).injectMembers(this);
        this.callback = callback;
    }

    @Override
    public Void doInBackground(Void... params)  {

        try {
            ListRoundsResult result = apiManager.getRounds();

        if (result != null && result.getRounds() != null && result.getRounds().size() > 0) {
            roundDao.store(result.getRounds());
        }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        riderManager.loadRidersFromServer();

        return null;
    }

    @Override
    public void onPostExecute(Void v) {
        callback.onSuccess();
    }

}
