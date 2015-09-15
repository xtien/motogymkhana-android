package eu.motogymkhana.competition.activity;

import android.content.Context;

import com.google.inject.Inject;

import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.api.ApiManager;
import roboguice.util.RoboAsyncTask;

/**
 * Created by christine on 10-6-15.
 */
public class CheckPasswordTask extends RoboAsyncTask<Boolean> {

    private final MyPasswordCallback callback;
    private String password;

    @Inject
    private ApiManager apiManager;

    public CheckPasswordTask(Context context, String password, MyPasswordCallback callback) {

        super(context);

        this.callback = callback;
        this.password = password;
    }

    @Override
    public Boolean call() throws Exception {

        boolean result = apiManager.checkPassword(Constants.CUSTOMER_CODE, password);
        return result;
    }

    @Override
    public void onSuccess(Boolean result) {

        callback.onResult(result);
    }


    @Override
    public void onException(Exception e) {
        e.printStackTrace();
        callback.onResult(false);
    }
}
