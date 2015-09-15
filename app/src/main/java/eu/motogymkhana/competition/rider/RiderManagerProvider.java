package eu.motogymkhana.competition.rider;

import android.content.Context;

import roboguice.RoboGuice;

/**
 * Created by christine on 26-5-15.
 */
public class RiderManagerProvider {

    private static RiderManager riderManager;

    private static Context context;

    public static RiderManager getInstance() {

        if (riderManager == null) {
            riderManager = RoboGuice.getInjector(context).getInstance(RiderManager.class);
        }

        return riderManager;
    }

    public static void setContext(Context c) {
        context = c;
    }
}
