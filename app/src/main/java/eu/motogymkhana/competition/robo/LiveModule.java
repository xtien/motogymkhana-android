package eu.motogymkhana.competition.robo;

import android.content.Context;

import com.google.inject.AbstractModule;

import eu.motogymkhana.competition.api.http.MyHttp;
import eu.motogymkhana.competition.api.http.impl.MyHttpImpl;
import eu.motogymkhana.competition.context.ContextProvider;
import eu.motogymkhana.competition.context.impl.ContextProviderImpl;

/**
 * Created by christine on 24-7-15.
 * This file contains Roboguice bindings that apply to the live app. These bindings are different
 * in unit tests.
 */
public class LiveModule extends AbstractModule {

    @Override
    protected void configure() {

        bind(Context.class).toProvider(CtxProvider.class);
        bind(MyHttp.class).to(MyHttpImpl.class).asEagerSingleton();
        bind(ContextProvider.class).to(ContextProviderImpl.class);
    }
}