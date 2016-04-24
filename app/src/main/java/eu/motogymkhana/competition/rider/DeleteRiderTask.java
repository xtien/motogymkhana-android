package eu.motogymkhana.competition.rider;

import android.content.Context;
import android.os.AsyncTask;

import com.google.inject.Inject;

import java.io.IOException;
import java.sql.SQLException;

import eu.motogymkhana.competition.api.ApiManager;
import eu.motogymkhana.competition.api.impl.RidersCallback;
import eu.motogymkhana.competition.dao.RiderDao;
import eu.motogymkhana.competition.model.Rider;
import roboguice.RoboGuice;

/**
 * Created by christine on 20-5-15.
 */
public class DeleteRiderTask extends AsyncTask<Void,Void,Void> {

    private final Rider rider;
    private final RidersCallback callback;

    @Inject
    private RiderDao riderDao;

    @Inject
    private ApiManager apiManager;

    public DeleteRiderTask(Context context, Rider rider, RidersCallback callback){
        RoboGuice.getInjector(context).injectMembers(this);
        this.rider = rider;
        this.callback = callback;
    }

    @Override
    public Void doInBackground(Void... params) {

        try {
            riderDao.deleteRider(rider);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            apiManager.delete(rider);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    @Override
    public void onPostExecute(Void v){
        callback.onSuccess();
    }
}
