package eu.motogymkhana.competition.rider;

import android.content.Context;

import com.google.inject.Inject;

import eu.motogymkhana.competition.api.ApiManager;
import eu.motogymkhana.competition.api.impl.RidersCallback;
import eu.motogymkhana.competition.dao.RiderDao;
import eu.motogymkhana.competition.model.Rider;
import roboguice.util.RoboAsyncTask;

/**
 * Created by christine on 20-5-15.
 */
public class DeleteRiderTask extends RoboAsyncTask<Void>{

    private final Rider rider;
    private final RidersCallback callback;

    @Inject
    private RiderDao riderDao;

    @Inject
    private ApiManager apiManager;

    public DeleteRiderTask(Context context, Rider rider, RidersCallback callback){
        super(context);
        this.rider = rider;
        this.callback = callback;
    }

    @Override
    public Void call() throws Exception {

        riderDao.deleteRider(rider);

        apiManager.delete(rider);

        return null;
    }
    @Override
    public void onSuccess(Void v){
        callback.onSuccess();
    }

    @Override
    public void onException(Exception e){
        callback.onError();
        e.printStackTrace();
    }
}
