package eu.motogymkhana.competition.rider;

import android.content.Context;

import com.google.inject.Inject;

import eu.motogymkhana.competition.api.ApiManager;
import roboguice.util.RoboAsyncTask;

public class SendTextTask extends RoboAsyncTask<Void> {

	@Inject
	private ApiManager apiManager;

	private String text;

	public SendTextTask(Context context, String text) {
		super(context);
		this.text=text;
	}

	@Override
	public Void call() throws Exception {

		apiManager.sendText(text);
		return null;
	}
}
