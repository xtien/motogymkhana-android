package eu.motogymkhana.competition.rider;

import android.content.Context;
import android.os.AsyncTask;

import com.google.inject.Inject;

import java.io.IOException;
import java.util.List;

import eu.motogymkhana.competition.api.ApiManager;
import eu.motogymkhana.competition.model.Rider;
import roboguice.RoboGuice;

/**
 * Created by christine on 13-5-15.
 */
public class UpdateRidersTask extends AsyncTask<Void,Void,Void> {

    private final List<Rider> riders;
    private final UpdateRiderCallback callback;

    @Inject
    private ApiManager apiManager;

    public UpdateRidersTask(Context context, List<Rider> riders, UpdateRiderCallback callback) {

        RoboGuice.getInjector(context).injectMembers(this);
        this.riders = riders;
        this.callback = callback;
    }

    @Override
    public Void doInBackground(Void... params) {

        try {
            apiManager.updateRiders(riders);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void onPostExecute(Void v) {

        callback.onSuccess();
    }
}
