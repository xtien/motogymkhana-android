package eu.motogymkhana.competition.rider;

import android.content.Context;

import com.google.inject.Inject;

import java.util.Collection;

import eu.motogymkhana.competition.dao.TimesDao;
import eu.motogymkhana.competition.model.Rider;
import roboguice.util.RoboAsyncTask;

/**
 * Created by christine on 7-9-15.
 */
public class GetRegisteredRidersFromDBTask extends RoboAsyncTask<Collection<Rider>> {

    @Inject
    private TimesDao timesDao;

    private final GetRidersCallback callback;
    private final long date;

    public GetRegisteredRidersFromDBTask(Context context, GetRidersCallback callback, long date) {

        super(context);
        this.callback = callback;
        this.date = date;
    }

    @Override
    public Collection<Rider> call() throws Exception {
        return timesDao.getRegisteredRiders(date);
    }

    @Override
    public void onSuccess(Collection<Rider> riders) {
        callback.onSuccess(riders);
    }
}
