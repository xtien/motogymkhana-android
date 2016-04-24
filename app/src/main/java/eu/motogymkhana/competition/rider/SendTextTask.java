package eu.motogymkhana.competition.rider;

import android.content.Context;
import android.os.AsyncTask;

import com.google.inject.Inject;

import java.io.IOException;

import eu.motogymkhana.competition.api.ApiManager;
import roboguice.RoboGuice;

public class SendTextTask extends AsyncTask<Void,Void,Void> {

	@Inject
	private ApiManager apiManager;

	private String text;

	public SendTextTask(Context context, String text) {

		RoboGuice.getInjector(context).injectMembers(this);
		this.text=text;
	}

	@Override
	public Void doInBackground(Void... params) {

		try {
			apiManager.sendText(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
