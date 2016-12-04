package eu.motogymkhana.competition.module;

import android.content.Context;

import eu.motogymkhana.competition.api.http.MyHttp;
import eu.motogymkhana.competition.api.http.impl.MyHttpImpl;
import toothpick.config.Module;

public class TestLiveServerModule extends Module {

	public TestLiveServerModule(Context context) {
		bind(MyHttp.class).to(MyHttpImpl.class);
	}
}
