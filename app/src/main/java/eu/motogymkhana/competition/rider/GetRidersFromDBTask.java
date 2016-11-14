package eu.motogymkhana.competition.rider;

import android.content.Context;
import android.os.AsyncTask;

import javax.inject.Inject;

import java.sql.SQLException;
import java.util.Collection;

import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.dao.RiderDao;
import eu.motogymkhana.competition.model.Rider;
import toothpick.Scope;
import toothpick.Toothpick;

/**
 * Created by christine on 7-9-15.
 */
public class GetRidersFromDBTask extends AsyncTask<Void, Void, Collection<Rider>> {

    private  Scope scope;
    @Inject
    protected RiderDao riderDao;

    private  GetRidersCallback callback;

    public GetRidersFromDBTask(){


    } public GetRidersFromDBTask(Context context, GetRidersCallback callback) {
        scope = Toothpick.openScopes(Constants.DEFAULT_SCOPE, this);
        Toothpick.inject(this, scope);
        this.callback = callback;
    }

    @Override
    public Collection<Rider> doInBackground(Void... params) {

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
        Toothpick.closeScope(scope);
    }
}
