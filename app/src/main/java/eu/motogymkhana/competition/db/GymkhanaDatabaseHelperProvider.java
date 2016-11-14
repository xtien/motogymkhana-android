package eu.motogymkhana.competition.db;

import android.content.Context;

import javax.inject.Inject;
import javax.inject.Provider;

public class GymkhanaDatabaseHelperProvider implements Provider<GymkhanaDatabaseHelper> {

    protected Context context;

    private GymkhanaDatabaseHelper helper;

    @Inject
    public GymkhanaDatabaseHelperProvider(Context context){
        this.context = context;
    }

    @Override
    public GymkhanaDatabaseHelper get() {

        if (helper == null) {
            helper = new GymkhanaDatabaseHelper(context);
        }

        return helper;
    }
}