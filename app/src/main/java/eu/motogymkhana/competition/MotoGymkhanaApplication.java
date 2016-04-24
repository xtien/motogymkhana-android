package eu.motogymkhana.competition;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import eu.motogymkhana.competition.robo.GymkhanaModule;
import eu.motogymkhana.competition.robo.LiveModule;
import roboguice.RoboGuice;

/**
 * Created by christine on 7-9-15.
 */
public class MotoGymkhanaApplication extends Application {

    private static Context instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = getApplicationContext();
        RoboGuice.setUseAnnotationDatabases(false);
        RoboGuice.setupBaseApplicationInjector(this, new GymkhanaModule(), new LiveModule());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static Context getContext() {
        return instance;
    }
}