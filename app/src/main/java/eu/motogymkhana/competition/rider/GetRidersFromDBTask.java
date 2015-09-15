package eu.motogymkhana.competition.rider;

import android.content.Context;

import com.google.inject.Inject;

import java.util.Collection;

import eu.motogymkhana.competition.dao.RiderDao;
import eu.motogymkhana.competition.model.Rider;
import roboguice.util.RoboAsyncTask;

/**
 * Created by christine on 7-9-15.
 */
public class GetRidersFromDBTask  extends RoboAsyncTask<Collection<Rider>> {

    @Inject
    private RiderDao riderDao;

    private final GetRidersCallback callback;

    public GetRidersFromDBTask(Context context, GetRidersCallback callback) {
        super(context);
        this.callback = callback;
    }

    @Override
    public Collection<Rider> call() throws Exception {

        return riderDao.getRiders();
    }

    @Override
    public void onSuccess(Collection<Rider> riders) {

        callback.onSuccess(riders);
    }
}
