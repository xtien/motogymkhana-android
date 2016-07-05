package eu.motogymkhana.competition.test;

import android.content.Context;

import com.google.inject.Inject;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import eu.motogymkhana.competition.BuildConfig;
import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.adapter.ChangeListener;
import eu.motogymkhana.competition.api.ResponseHandler;
import eu.motogymkhana.competition.context.ContextProvider;
import eu.motogymkhana.competition.dao.RiderDao;
import eu.motogymkhana.competition.dao.RoundDao;
import eu.motogymkhana.competition.http.FakeHttp;
import eu.motogymkhana.competition.model.Rider;
import eu.motogymkhana.competition.model.Round;
import eu.motogymkhana.competition.notify.Notifier;
import eu.motogymkhana.competition.rider.GetRidersCallback;
import eu.motogymkhana.competition.rider.RiderManager;
import eu.motogymkhana.competition.robo.RoboInjectedTestRunner;

/**
 * Created by christine on 24-7-15.
 */
@RunWith(RoboInjectedTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class RidersApiTest {

    @SuppressWarnings("unused")
    @Inject
    private ContextProvider contextProvider;

    @Inject
    private RiderManager riderManager;

    @Inject
    private Context context;

    @Inject
    private FakeHttp fakeHttp;

    @Inject
    private RoundDao roundDao;

    @Inject
    private Notifier notifier;

    @Inject
    private RiderDao riderDao;

    String dateOne = "01-06-2016";
    String dateTwo = "01-07-2016";

    private String ridersUrlString = "https://api.gymcomp.com:9005/motogymkhana/getRiders/";
    private String ridersJsonFile = "get_riders.json";

    private volatile boolean done = false;

    private List<Rider> riders = new LinkedList<Rider>();

    GetRidersCallback callback = new GetRidersCallback() {

        @Override
        public void onSuccess(Collection<Rider> collection) {
            riders.clear();
            riders.addAll(collection);
            done = true;
        }

        @Override
        public void onError(String error) {
            done = true;
        }
    };

    private ResponseHandler downloadRidersResponseHandler = new ResponseHandler() {

        @Override
        public void onSuccess(Object object) {
            done = true;
        }

        @Override
        public void onException(Exception e) {
            done = true;
        }

        @Override
        public void onError(int statusCode, String string) {
            done = true;
        }
    };

    @Test
    public void testGetRiders() throws IOException, InterruptedException, ParseException, SQLException {

        Assert.assertNotNull(context);

        Round roundOne = new Round();
        roundOne.setDate(Constants.dateFormat.parse(dateOne).getTime());
        roundOne.setCountry(Constants.country);
        roundOne.setSeason(Constants.season);
        Round roundTwo = new Round();
        roundTwo.setDate(Constants.dateFormat.parse(dateTwo).getTime());
        roundTwo.setCountry(Constants.country);
        roundTwo.setSeason(Constants.season);

        roundDao.store(roundOne);
        roundDao.store(roundTwo);

        fakeHttp.put(ridersUrlString, 200, "", ridersJsonFile);

        notifier.registerRiderResultListener(new ChangeListener() {

            @Override
            public void notifyDataChanged() {
                done = true;
            }
        });

        riderManager.downloadRiders(downloadRidersResponseHandler);

        while (!done) {
            Thread.sleep(1000);
        }

        done = false;

        riderManager.getRiders(callback);
        while (!done) {
            Thread.sleep(1000);
        }

        Assert.assertEquals(31, riders.size());
    }
}
