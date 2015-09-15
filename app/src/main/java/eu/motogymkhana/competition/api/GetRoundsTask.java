package eu.motogymkhana.competition.api;

import android.content.Context;

import com.google.inject.Inject;

import eu.motogymkhana.competition.api.impl.RidersCallback;
import eu.motogymkhana.competition.dao.RoundDao;
import eu.motogymkhana.competition.round.RoundManager;
import roboguice.util.RoboAsyncTask;

public class GetRoundsTask extends RoboAsyncTask<Void> {

    @Inject
    private ApiManager apiManager;

    @Inject
    private RoundDao roundDao;

    @Inject
    private RoundManager roundManager;

    private RidersCallback callback;

    public GetRoundsTask(Context context, RidersCallback callback) {
        super(context);
        this.callback = callback;
    }

    @Override
    public Void call() throws Exception {

        ListRoundsResult result = apiManager.getRounds();

        if (result.getRounds() != null && result.getRounds().size()>0) {
            roundDao.store(result.getRounds());
        }

        return null;
    }

    @Override
    public void onSuccess(Void v) {
        callback.onSuccess();
    }

}
