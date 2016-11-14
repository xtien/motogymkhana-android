package eu.motogymkhana.competition.robo;

import android.content.Context;

import eu.motogymkhana.competition.api.http.MyHttp;
import eu.motogymkhana.competition.api.http.impl.MyHttpImpl;
import toothpick.config.Module;

/**
 * Created by christine on 24-7-15.
 * This file contains  bindings that apply to the live app. These bindings are different
 * in unit tests.
 */
public class LiveModule extends Module {

    public LiveModule(final Context context) {

        bind(Context.class).toInstance(context);

        bind(Context.class).toProvider(CtxProvider.class);
        bind(MyHttp.class).to(MyHttpImpl.class);
    }
}