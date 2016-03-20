package eu.motogymkhana.competition.settings;

import android.content.Context;

import com.google.inject.Inject;

import java.util.Collection;

import eu.motogymkhana.competition.api.ApiManager;
import eu.motogymkhana.competition.model.Round;
import eu.motogymkhana.competition.rider.UpdateRiderCallback;
import roboguice.util.RoboAsyncTask;

/**
 * Created by christine on 13-5-15.
 */
public class UploadSettingsTask extends RoboAsyncTask<Void> {

    private final Settings settings;
    private final UpdateRiderCallback callback;

    @Inject
    private ApiManager apiManager;

    public UploadSettingsTask(Context context, Settings settings, UpdateRiderCallback callback) {
        super(context);

        this.settings = settings;
        this.callback = callback;
    }

    @Override
    public Void call() throws Exception {

        apiManager.uploadSettings(settings);

        return null;
    }

    @Override
    public void onSuccess(Void v) {

        if (callback != null) {
            callback.onSuccess();
        }
    }

    @Override
    public void onException(Exception e) {

        if (callback != null) {
            callback.onError(e.getMessage());
        }
    }
}
