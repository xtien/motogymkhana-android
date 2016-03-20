package eu.motogymkhana.competition.robo;

import com.google.inject.AbstractModule;

import eu.motogymkhana.competition.api.http.MyHttp;
import eu.motogymkhana.competition.context.ContextProvider;
import eu.motogymkhana.competition.context.impl.ContextProviderImpl;
import eu.motogymkhana.competition.http.FakeHttp;
import eu.motogymkhana.competition.http.impl.FakeHttpImpl;
import eu.motogymkhana.competition.http.impl.TestMyHttpImpl;
import eu.motogymkhana.competition.util.FileAssetManager;
import eu.motogymkhana.competition.util.FileAssetManagerImpl;

public class TestModule extends AbstractModule {

	@Override
	protected void configure() {

		bind(ContextProvider.class).to(ContextProviderImpl.class);

		bind(MyHttp.class).to(TestMyHttpImpl.class).asEagerSingleton();
		bind(FakeHttp.class).to(FakeHttpImpl.class);

		bind(FileAssetManager.class).to(FileAssetManagerImpl.class);
	}
}
