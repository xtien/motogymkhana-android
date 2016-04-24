package eu.motogymkhana.competition.round;

import android.content.Context;
import android.os.AsyncTask;

import com.google.inject.Inject;

import java.io.IOException;
import java.util.Collection;

import eu.motogymkhana.competition.api.ApiManager;
import eu.motogymkhana.competition.model.Round;
import eu.motogymkhana.competition.rider.UpdateRiderCallback;
import roboguice.RoboGuice;

/**
 * Created by christine on 13-5-15.
 */
public class UploadRoundsTask extends AsyncTask<Void,Void,Void> {

    private final Collection<Round> rounds;
    private final UpdateRiderCallback callback;

    @Inject
    private ApiManager apiManager;

    public UploadRoundsTask(Context context, Collection<Round> rounds, UpdateRiderCallback callback) {

        RoboGuice.getInjector(context).injectMembers(this);
        this.rounds = rounds;
        this.callback = callback;
    }

    @Override
    public Void doInBackground(Void... params)  {

        try {
            apiManager.uploadRounds(rounds);
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
