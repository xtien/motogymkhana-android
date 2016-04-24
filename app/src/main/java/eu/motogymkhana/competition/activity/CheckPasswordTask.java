package eu.motogymkhana.competition.activity;

import android.content.Context;
import android.os.AsyncTask;

import com.google.inject.Inject;

import java.io.IOException;

import eu.motogymkhana.competition.api.ApiManager;
import roboguice.RoboGuice;

/**
 * Created by christine on 10-6-15.
 */
public class CheckPasswordTask extends AsyncTask<Void,Void,Boolean> {

    private final MyPasswordCallback callback;
    private String password;

    @Inject
    private ApiManager apiManager;

    public CheckPasswordTask(Context context, String password, MyPasswordCallback callback) {

        RoboGuice.getInjector(context).injectMembers(this);

        this.callback = callback;
        this.password = password;
    }

    @Override
    public Boolean doInBackground(Void... params)  {

        boolean result = false;
        try {
            result = apiManager.checkPassword(password);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void onPostExecute(Boolean result) {

        callback.onResult(result);
    }
}
