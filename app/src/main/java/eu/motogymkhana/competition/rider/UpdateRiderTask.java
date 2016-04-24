package eu.motogymkhana.competition.rider;

import android.content.Context;
import android.os.AsyncTask;

import com.google.inject.Inject;

import java.io.IOException;
import java.sql.SQLException;

import eu.motogymkhana.competition.model.Rider;
import roboguice.RoboGuice;

public class UpdateRiderTask extends AsyncTask<Void,Void,Void> {

    private final UpdateRiderCallback callback;
    @Inject
    private RiderManager riderManager;

    private Rider rider;

    public UpdateRiderTask(Context context, Rider rider, UpdateRiderCallback callback) {
        RoboGuice.getInjector(context).injectMembers(this);
        this.rider = rider;
        this.callback = callback;
    }

    @Override
    public Void doInBackground(Void... params)   {

        try {
            riderManager.update(rider);
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
