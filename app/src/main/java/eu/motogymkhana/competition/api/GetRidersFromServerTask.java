package eu.motogymkhana.competition.api;

import android.content.Context;

import com.google.inject.Inject;

import eu.motogymkhana.competition.api.impl.RidersCallback;
import eu.motogymkhana.competition.dao.RiderDao;
import eu.motogymkhana.competition.rider.RiderManager;
import roboguice.util.RoboAsyncTask;

public class GetRidersFromServerTask extends RoboAsyncTask<Void> {

    @Inject
    private ApiManager apiManager;

    @Inject
    private RiderDao riderDao;

    @Inject
    private RiderManager riderManager;

    private RidersCallback callback;

    public GetRidersFromServerTask(Context context, RidersCallback callback) {
        super(context);
        this.callback = callback;
    }

    @Override
    public Void call() throws Exception {

        ListRidersResult result = apiManager.getRiders();

        if (result.getRiders() != null) {
            riderDao.store(result.getRiders());
        }

        riderManager.setMessageText(result.getText());

        return null;
    }

    @Override
    public void onSuccess(Void v) {
        callback.onSuccess();
    }

}
