package eu.motogymkhana.competition.round;

import android.content.Context;

import roboguice.RoboGuice;

/**
 * Created by christine on 26-5-15.
 */
public class RoundManagerProvider {

    private static RoundManager roundManager;

    private static Context context;

    public static RoundManager getInstance() {

        if (roundManager == null) {
            roundManager = RoboGuice.getInjector(context).getInstance(RoundManager.class);
        }

        return roundManager;
    }

    public static void setContext(Context c) {
        context = c;
    }
}
