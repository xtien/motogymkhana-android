package eu.motogymkhana.competition.rider;

import android.content.Context;

import com.google.inject.Inject;

import eu.motogymkhana.competition.dao.TimesDao;
import eu.motogymkhana.competition.model.Rider;
import eu.motogymkhana.competition.model.Times;
import eu.motogymkhana.competition.round.RoundManager;
import roboguice.util.RoboAsyncTask;

/**
 * Created by christine on 28-5-15.
 */
public class SetRegisteredTask extends RoboAsyncTask<Void> {

    private final Rider rider;
    private final boolean registered;

    @Inject
    private RoundManager roundManager;

    @Inject
    private RiderManager riderManager;

    @Inject
    private TimesDao timesDao;

    public SetRegisteredTask(Context context, Rider rider, boolean registered) {
        super(context);

        this.rider = rider;
        this.registered = registered;
    }

    @Override
    public Void call() throws Exception {

        Times times = rider.getEUTimes(roundManager.getDate());
        times.setRegistered(registered);

        new UpdateRiderTask(context, rider, new UpdateRiderCallback() {

            @Override
            public void onSuccess() {
                riderManager.notifyDataChanged();
            }

            @Override
            public void onError(String error) {

            }
        }).execute();


        return null;
    }
}
