package eu.motogymkhana.competition.rider;

import android.content.Context;

import com.google.inject.Inject;

import java.util.List;

import eu.motogymkhana.competition.api.ApiManager;
import eu.motogymkhana.competition.model.Rider;
import roboguice.util.RoboAsyncTask;

/**
 * Created by christine on 13-5-15.
 */
public class UploadRidersTask extends RoboAsyncTask<Void> {

    private final List<Rider> riders;
    private final UpdateRiderCallback callback;

    @Inject
    private ApiManager apiManager;

    public UploadRidersTask(Context context, List<Rider> riders, UpdateRiderCallback callback) {
        super(context);

        this.riders = riders;
        this.callback = callback;
    }

    @Override
    public Void call() throws Exception {

        apiManager.uploadRiders(riders);

        return null;
    }

    @Override
    public void onSuccess(Void v) {

        callback.onSuccess();
    }

    @Override
    public void onException(Exception e) {
        callback.onError(e.getMessage());
    }
}
