package eu.motogymkhana.competition.test;

import android.content.Context;
import android.os.AsyncTask;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLooper;

import java.sql.SQLException;
import java.util.Collection;

import javax.inject.Inject;

import eu.motogymkhana.competition.BuildConfig;
import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.api.ResponseHandler;
import eu.motogymkhana.competition.http.FakeHttp;
import eu.motogymkhana.competition.model.Round;
import eu.motogymkhana.competition.round.RoundManager;
import toothpick.Scope;
import toothpick.Toothpick;

/**
 * Created by christine on 24-7-15.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class RoundsApiTest {

    @Inject
    protected RoundManager roundManager;

    @Inject
    protected Context context;

    @Inject
    protected FakeHttp fakeHttp;

    private String urlString = "https://pengo.christine.nl:9005/motogymkhana/getRounds/";
    private String resultFileName = "test/get_rounds.json";
    private String uploadRoundsUrlString = "https://pengo.christine.nl:9005/motogymkhana/uploadRounds/";
    private String uploadRoundsResultFileName = "test/get_rounds.json";
    private String ridersUrlString = "https://pengo.christine.nl:9005/motogymkhana/getRiders/";
    private String ridersJsonFile = "test/get_riders.json";

    private boolean done;

    private ResponseHandler loadRoundsResponseHandler = new ResponseHandler() {
        @Override
        public void onSuccess(Object object) {
            done = true;
        }

        @Override
        public void onException(Exception e) {

        }

        @Override
        public void onError(int statusCode, String string) {

        }
    };

    @Test
    public void testGetDates() throws SQLException, InterruptedException {

        Scope scope = Toothpick.openScope(Constants.TEST_SCOPE);
        Toothpick.inject(this, scope);

        fakeHttp.put(urlString, 200, "", resultFileName);
        fakeHttp.put(ridersUrlString, 200, "", ridersJsonFile);
        fakeHttp.put(uploadRoundsUrlString, 200, "", uploadRoundsResultFileName);

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                roundManager.loadRoundsFromServer(loadRoundsResponseHandler);
                return null;
            }
        }.execute();

        Collection<Round> rounds = null;

        while (!done) {
            Thread.sleep(1000);
            ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
        }

        Assert.assertEquals(8, rounds.size());
    }
}
