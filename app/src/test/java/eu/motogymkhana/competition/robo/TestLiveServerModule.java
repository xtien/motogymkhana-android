package eu.motogymkhana.competition.robo;

import android.content.Context;

import toothpick.config.Module;

public class TestLiveServerModule extends Module {

	public TestLiveServerModule(Context context) {
		bind(Context.class).toProvider(TestCtxProvider.class);
	}
}
