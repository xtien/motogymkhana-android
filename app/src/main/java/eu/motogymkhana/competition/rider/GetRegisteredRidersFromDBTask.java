package eu.motogymkhana.competition.rider;

import android.content.Context;
import android.os.AsyncTask;

import com.google.inject.Inject;

import java.sql.SQLException;
import java.util.Collection;

import eu.motogymkhana.competition.dao.TimesDao;
import eu.motogymkhana.competition.model.Rider;
import roboguice.RoboGuice;

/**
 * Created by christine on 7-9-15.
 */
public class GetRegisteredRidersFromDBTask extends AsyncTask<Void, Void, Collection<Rider>> {

    @Inject
    private TimesDao timesDao;

    private final GetRidersCallback callback;
    private final long date;

    public GetRegisteredRidersFromDBTask(Context context, GetRidersCallback callback, long date) {

        RoboGuice.getInjector(context).injectMembers(this);
        this.callback = callback;
        this.date = date;
    }

    @Override
    public Collection<Rider> doInBackground(Void... params) {
        try {
            return timesDao.getRegisteredRiders(date);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onPostExecute(Collection<Rider> riders) {
        callback.onSuccess(riders);
    }
}
