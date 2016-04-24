package eu.motogymkhana.competition.rider;

import android.content.Context;
import android.os.AsyncTask;

import com.google.inject.Inject;

import java.sql.SQLException;
import java.util.Collection;

import eu.motogymkhana.competition.dao.RiderDao;
import eu.motogymkhana.competition.model.Rider;
import roboguice.RoboGuice;

/**
 * Created by christine on 7-9-15.
 */
public class GetRidersFromDBTask  extends AsyncTask<Void,Void,Collection<Rider>> {

    @Inject
    private RiderDao riderDao;

    private final GetRidersCallback callback;

    public GetRidersFromDBTask(Context context, GetRidersCallback callback) {
        RoboGuice.getInjector(context).injectMembers(this);
        this.callback = callback;
    }

    @Override
    public Collection<Rider> doInBackground(Void... params)  {

        try {
            return riderDao.getRiders();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onPostExecute(Collection<Rider> riders) {

        callback.onSuccess(riders);
    }
}
