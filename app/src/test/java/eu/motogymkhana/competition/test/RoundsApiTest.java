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

    private String urlString = "https://api.gymcomp.com:9005/motogymkhana/getRounds/";
    private String resultFileName = "test/get_rounds.json";
    private String uploadRoundsUrlString = "https://api.gymcomp.com:9005/motogymkhana/uploadRounds/";
    private String uploadRoundsResultFileName = "test/get_rounds.json";
    private String ridersUrlString = "https://api.gymcomp.com:9005/motogymkhana/getRiders/";
    private String ridersJsonFile = "test/get_riders.json";

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
                roundManager.loadRoundsFromServer();
                return null;
            }
        }.execute();

        Collection<Round> rounds = null;

        while (rounds == null || rounds.size() < 8) {
            Thread.sleep(1000);
            rounds = roundManager.getRounds();
            ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
        }

        Assert.assertEquals(8, rounds.size());
    }
}
