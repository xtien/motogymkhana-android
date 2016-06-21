package eu.motogymkhana.competition.robo;

import android.content.Context;

import com.google.inject.AbstractModule;

import eu.motogymkhana.competition.context.ContextProvider;
import eu.motogymkhana.competition.context.impl.ContextProviderImpl;

public class TestLiveServerModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Context.class).toProvider(TestCtxProvider.class);
		bind(ContextProvider.class).to(ContextProviderImpl.class);
	}
}
