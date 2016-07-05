package eu.motogymkhana.competition.test;

import android.content.Context;

import com.google.inject.Inject;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import java.sql.SQLException;
import java.util.Collection;

import eu.motogymkhana.competition.BuildConfig;
import eu.motogymkhana.competition.context.ContextProvider;
import eu.motogymkhana.competition.http.FakeHttp;
import eu.motogymkhana.competition.model.Round;
import eu.motogymkhana.competition.robo.RoboInjectedTestRunner;
import eu.motogymkhana.competition.round.RoundManager;

/**
 * Created by christine on 24-7-15.
 */
@RunWith(RoboInjectedTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class RoundsApiTest {

    @SuppressWarnings("unused")
    @Inject
    private ContextProvider contextProvider;

    @Inject
    private RoundManager roundManager;

    @Inject
    private Context context;

    @Inject
    private FakeHttp fakeHttp;

    private String urlString = "https://api.gymcomp.com:9005/motogymkhana/getRounds/";
    private String resultFileName = "get_rounds.json";
    private String uploadRoundsUrlString = "https://api.gymcomp.com:9005/motogymkhana/uploadRounds/";
    private String uploadRoundsResultFileName = "get_rounds.json";
    private String ridersUrlString = "https://api.gymcomp.com:9005/motogymkhana/getRiders/";
    private String ridersJsonFile = "get_riders.json";

    @Test
    public void testGetDates() throws SQLException, InterruptedException {

        fakeHttp.put(urlString, 200, "", resultFileName);
        fakeHttp.put(ridersUrlString, 200, "", ridersJsonFile);
        fakeHttp.put(uploadRoundsUrlString, 200, "", uploadRoundsResultFileName);

        roundManager.loadRoundsFromServer();

        Collection<Round> rounds = null;

        while (rounds == null || rounds.size() < 8) {
            Thread.sleep(1000);
            rounds = roundManager.getRounds();
        }

        Assert.assertEquals(8, rounds.size());
    }
}
