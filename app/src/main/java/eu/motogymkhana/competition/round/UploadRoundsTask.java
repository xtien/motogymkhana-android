package eu.motogymkhana.competition.round;

import android.content.Context;

import com.google.inject.Inject;

import java.util.Collection;

import eu.motogymkhana.competition.api.ApiManager;
import eu.motogymkhana.competition.model.Round;
import eu.motogymkhana.competition.rider.UpdateRiderCallback;
import roboguice.util.RoboAsyncTask;

/**
 * Created by christine on 13-5-15.
 */
public class UploadRoundsTask extends RoboAsyncTask<Void> {

    private final Collection<Round> rounds;
    private final UpdateRiderCallback callback;

    @Inject
    private ApiManager apiManager;

    public UploadRoundsTask(Context context, Collection<Round> rounds, UpdateRiderCallback callback) {
        super(context);

        this.rounds = rounds;
        this.callback = callback;
    }

    @Override
    public Void call() throws Exception {

        apiManager.uploadRounds(rounds);

        return null;
    }

    @Override
    public void onSuccess(Void v) {

        if (callback != null) {
            callback.onSuccess();
        }
    }

    @Override
    public void onException(Exception e) {

        if (callback != null) {
            callback.onError(e.getMessage());
        }
    }
}
