package eu.motogymkhana.competition.settings;

import android.content.Context;
import android.os.AsyncTask;

import com.google.inject.Inject;

import java.io.IOException;

import eu.motogymkhana.competition.api.ApiManager;
import eu.motogymkhana.competition.rider.UpdateRiderCallback;
import roboguice.RoboGuice;

/**
 * Created by christine on 13-5-15.
 */
public class UploadSettingsTask extends AsyncTask<Void,Void,Void> {

    private final Settings settings;
    private final UpdateRiderCallback callback;

    @Inject
    private ApiManager apiManager;

    public UploadSettingsTask(Context context, Settings settings, UpdateRiderCallback callback) {

        RoboGuice.getInjector(context).injectMembers(this);

        this.settings = settings;
        this.callback = callback;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            apiManager.uploadSettings(settings);
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
