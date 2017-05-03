package eu.motogymkhana.competition.test;

import android.content.Context;
import android.util.Log;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLooper;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import eu.motogymkhana.competition.BuildConfig;
import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.adapter.ChangeListener;
import eu.motogymkhana.competition.api.ResponseHandler;
import eu.motogymkhana.competition.api.response.ListRidersResult;
import eu.motogymkhana.competition.dao.RiderDao;
import eu.motogymkhana.competition.dao.RoundDao;
import eu.motogymkhana.competition.http.FakeHttp;
import eu.motogymkhana.competition.model.Rider;
import eu.motogymkhana.competition.model.Round;
import eu.motogymkhana.competition.notify.Notifier;
import eu.motogymkhana.competition.rider.GetRidersCallback;
import eu.motogymkhana.competition.rider.RiderManager;
import toothpick.Scope;
import toothpick.Toothpick;

/**
 * Created by christine on 24-7-15.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(packageName = "eu.motogymkhana.competition", constants = BuildConfig.class, sdk = 21)
public class RidersApiTest {

    @Inject
    protected RiderManager riderManager;

    @Inject
    protected Context context;

    @Inject
    protected FakeHttp fakeHttp;

    @Inject
    protected RoundDao roundDao;

    @Inject
    protected Notifier notifier;

    @Inject
    protected RiderDao riderDao;

    String dateOne = "01-06-2016";
    String dateTwo = "01-07-2016";

    private String ridersUrlString = "https://pengo.christine.nl:9005/motogymkhana/getRiders/";
    private String ridersJsonFile = "test/new_get_riders.json";

    private volatile boolean done = false;
    private ListRidersResult result;

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
             result = (ListRidersResult)object;
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

       // System.out.println("start");
        Scope scope = Toothpick.openScope(Constants.TEST_SCOPE);
        Toothpick.inject(this, scope);

       // System.out.println("after toothpick");

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
       // System.out.println("downloadRiders");

         riderManager.downloadRiders(downloadRidersResponseHandler);
       // System.out.println("after downloadRiders");

        while (!done) {
            Thread.sleep(100);
            ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
        }
        //System.out.println("assert");

        Assert.assertEquals(30, result.getRiders().size());

        List<Rider> allRiders = riderDao.getAllRiders();
        Assert.assertEquals(30, allRiders.size());

        done = false;

        riderManager.getRiders(callback);
        while (!done) {
            Thread.sleep(1000);
            ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
        }

        Assert.assertEquals(30, riders.size());
    }
}
