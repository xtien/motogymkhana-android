package eu.motogymkhana.competition;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import roboguice.RoboGuice;

/**
 * Created by christine on 7-9-15.
 */
public class MotoGymkhanaApplication extends Application {

    public MotoGymkhanaApplication() {
        RoboGuice.setUseAnnotationDatabases(false);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}