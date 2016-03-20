package eu.motogymkhana.competition.test;

import android.content.Context;

import com.google.inject.Inject;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.sql.SQLException;
import java.util.Collection;

import eu.motogymkhana.competition.context.ContextProvider;
import eu.motogymkhana.competition.http.FakeHttp;
import eu.motogymkhana.competition.model.Round;
import eu.motogymkhana.competition.robo.RoboInjectedTestRunner;
import eu.motogymkhana.competition.round.RoundManager;

/**
 * Created by christine on 24-7-15.
 */
@RunWith(RoboInjectedTestRunner.class)
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

    private String urlString = "https://www.christine.nl:9005/motogymkhana/NL/getRounds/";
    private String resultFileName = "get_rounds.json";

    @Test
    public void testGetDates() throws SQLException, InterruptedException {

        fakeHttp.put(urlString, 200, "", resultFileName);

        roundManager.loadRoundsFromServer();

        Collection<Round> rounds = null;

        while (rounds == null || rounds.size() < 8) {
            Thread.sleep(1000);
            rounds = roundManager.getRounds();
        }

        Assert.assertEquals(8, rounds.size());
    }
}
