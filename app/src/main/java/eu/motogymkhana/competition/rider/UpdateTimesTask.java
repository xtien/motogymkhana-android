package eu.motogymkhana.competition.rider;

import android.content.Context;

import com.google.inject.Inject;

import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.model.Rider;
import eu.motogymkhana.competition.model.Times;
import roboguice.util.RoboAsyncTask;

public class UpdateTimesTask extends RoboAsyncTask<Void> {

    private final UpdateRiderCallback callback;

    @Inject
    private RiderManager riderManager;

    private Times times;

    public UpdateTimesTask(Context context, Times times, UpdateRiderCallback callback) {
        super(context);
        this.times = times;
        this.callback = callback;
    }

    @Override
    public Void call() throws Exception {

        times.setRiderNumber(times.getRider().getRiderNumber());
        times.setSeason(Constants.season);
        times.setCountry(Constants.country);
        riderManager.update(times);
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
