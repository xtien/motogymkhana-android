package eu.motogymkhana.competition.db;

import android.content.Context;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class GymkhanaDatabaseHelperProvider implements Provider<GymkhanaDatabaseHelper> {

	@Inject
	protected Context context;

	private GymkhanaDatabaseHelper helper;

	@Override
	public GymkhanaDatabaseHelper get() {

		if (helper == null) {
			helper = new GymkhanaDatabaseHelper(context);
		}

		return helper;
	}
}