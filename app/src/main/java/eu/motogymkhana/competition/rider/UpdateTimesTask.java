package eu.motogymkhana.competition.rider;

import android.content.Context;
import android.os.AsyncTask;

import com.google.inject.Inject;

import java.io.IOException;
import java.sql.SQLException;

import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.model.Times;
import roboguice.RoboGuice;

public class UpdateTimesTask extends AsyncTask<Void,Void,Void> {

    private final UpdateRiderCallback callback;

    @Inject
    private RiderManager riderManager;

    private Times times;

    public UpdateTimesTask(Context context, Times times, UpdateRiderCallback callback) {

        RoboGuice.getInjector(context).injectMembers(this);

        this.times = times;
        this.callback = callback;
    }

    @Override
    public Void doInBackground(Void... params)  {

        times.setRiderNumber(times.getRider().getRiderNumber());
        times.setSeason(Constants.season);
        times.setCountry(Constants.country);
        try {
            riderManager.update(times);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onPostExecute(Void v) {

        if (callback != null) {
            callback.onSuccess();
        }
    }
}
