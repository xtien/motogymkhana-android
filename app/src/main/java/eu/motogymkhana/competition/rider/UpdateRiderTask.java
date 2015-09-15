package eu.motogymkhana.competition.rider;

import android.content.Context;

import com.google.inject.Inject;

import eu.motogymkhana.competition.model.Rider;
import roboguice.util.RoboAsyncTask;

public class UpdateRiderTask extends RoboAsyncTask<Void> {

    private final UpdateRiderCallback callback;
    @Inject
    private RiderManager riderManager;

    private Rider rider;

    public UpdateRiderTask(Context context, Rider rider, UpdateRiderCallback callback) {
        super(context);
        this.rider = rider;
        this.callback = callback;
    }

    @Override
    public Void call() throws Exception {

        riderManager.update(rider);

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
        e.printStackTrace();
    }

}
