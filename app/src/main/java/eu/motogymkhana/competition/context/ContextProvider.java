package eu.motogymkhana.competition.context;

import android.content.Context;

/**
 * In unit tests, you need to inject this class, even if you don't use it.
 * Roboguice needs to instantiate it before your app calls getContext().
 *
 * @author christine
 *
 */
public class ContextProvider {

	protected static Context context;

	public static Context getContext() {
		return context;
	}
}
