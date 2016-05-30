package eu.motogymkhana.competition.rider;

import android.content.Context;
import android.os.AsyncTask;

import com.google.inject.Inject;

import java.io.IOException;
import java.sql.SQLException;

import eu.motogymkhana.competition.dao.TimesDao;
import eu.motogymkhana.competition.model.Times;
import eu.motogymkhana.competition.round.RoundManager;
import roboguice.RoboGuice;

/**
 * Created by christine on 28-5-15.
 */
public class SetRegisteredTask extends AsyncTask<Void, Void, Void> {

    private final Times times;
    private final boolean registered;
    private final Context context;

    @Inject
    private RiderManager riderManager;

    public SetRegisteredTask(Context context, Times times, boolean registered) {

        RoboGuice.getInjector(context).injectMembers(this);
        this.times = times;
        this.registered = registered;
        this.context = context;
    }

    @Override
    public Void doInBackground(Void... params) {

        times.setRegistered(registered);
        return null;
    }

    @Override
    public void onPostExecute(Void v) {
        new UpdateTimesTask(context, times, new UpdateRiderCallback() {

            @Override
            public void onSuccess() {
                riderManager.notifyDataChanged();
            }

            @Override
            public void onError(String error) {

            }
        }).execute();
    }
}
