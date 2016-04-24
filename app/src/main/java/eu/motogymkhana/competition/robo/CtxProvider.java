package eu.motogymkhana.competition.robo;

import android.content.Context;

import com.google.inject.Provider;

import eu.motogymkhana.competition.MotoGymkhanaApplication;

/**
 * Created by christine on 21-4-16.
 */
public class CtxProvider implements Provider<Context> {

    @Override
    public Context get() {
        return MotoGymkhanaApplication.getContext();
    }
}
