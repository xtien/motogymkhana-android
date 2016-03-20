package eu.motogymkhana.competition.test;

import android.content.Context;

import com.google.inject.Inject;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import eu.motogymkhana.competition.adapter.ChangeListener;
import eu.motogymkhana.competition.context.ContextProvider;
import eu.motogymkhana.competition.http.FakeHttp;
import eu.motogymkhana.competition.model.Rider;
import eu.motogymkhana.competition.rider.GetRidersCallback;
import eu.motogymkhana.competition.rider.RiderManager;
import eu.motogymkhana.competition.rider.RiderManagerProvider;
import eu.motogymkhana.competition.robo.RoboInjectedTestRunner;
import eu.motogymkhana.competition.round.RoundManagerProvider;

/**
 * Created by christine on 24-7-15.
 */
@RunWith(RoboInjectedTestRunner.class)
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

    private String ridersUrlString = "https://www.christine.nl:9005/motogymkhana/NL/getRiders/";
    private String ridersJsonFile = "get_riders.json";

    private volatile boolean done = false;

    private List<Rider> riders;

    GetRidersCallback callback = new GetRidersCallback() {

        @Override
        public void onSuccess(Collection<Rider> collection) {
            riders.clear();
            riders.addAll(collection);
        }

        @Override
        public void onError(String error) {

        }
    };

    @Test
    public void testGetRiders() throws IOException, InterruptedException {

        Assert.assertNotNull(context);

        RiderManagerProvider.setContext(context);
        RoundManagerProvider.setContext(context);

        fakeHttp.put(ridersUrlString, 200, "", ridersJsonFile);

        riderManager.registerRiderResultListener(new ChangeListener() {

            @Override
            public void notifyDataChanged() {
                done = true;
            }
        });

        riderManager.downloadRiders();

        riders = null;

        while (!done) {
            Thread.sleep(1000);
            riderManager.getRiders(callback);
        }

        Assert.assertEquals(31, riders.size());
    }
}
