package eu.motogymkhana.competition.robo;

import android.content.Context;

import com.google.inject.AbstractModule;

import eu.motogymkhana.competition.adapter.RiderRegistrationListAdapter;
import eu.motogymkhana.competition.adapter.RiderResultListAdapter;
import eu.motogymkhana.competition.adapter.RiderTimeInputListAdapter;
import eu.motogymkhana.competition.adapter.TotalsListAdapter;
import eu.motogymkhana.competition.api.ApiManager;
import eu.motogymkhana.competition.api.ApiUrlHelper;
import eu.motogymkhana.competition.api.impl.ApiManagerImpl;
import eu.motogymkhana.competition.api.impl.ApiUrlHelperImpl;
import eu.motogymkhana.competition.dao.CredentialDao;
import eu.motogymkhana.competition.dao.RiderDao;
import eu.motogymkhana.competition.dao.RoundDao;
import eu.motogymkhana.competition.dao.SettingsDao;
import eu.motogymkhana.competition.dao.TimesDao;
import eu.motogymkhana.competition.dao.provider.CredentialDaoProvider;
import eu.motogymkhana.competition.dao.provider.RiderDaoProvider;
import eu.motogymkhana.competition.dao.provider.RoundDaoProvider;
import eu.motogymkhana.competition.dao.provider.SettingsDaoProvider;
import eu.motogymkhana.competition.dao.provider.TimesDaoProvider;
import eu.motogymkhana.competition.db.GymkhanaDatabaseHelper;
import eu.motogymkhana.competition.db.GymkhanaDatabaseHelperProvider;
import eu.motogymkhana.competition.prefs.ChristinePreferences;
import eu.motogymkhana.competition.prefs.impl.ChristinePreferencesImpl;
import eu.motogymkhana.competition.rider.RiderManager;
import eu.motogymkhana.competition.rider.impl.RiderManagerImpl;
import eu.motogymkhana.competition.round.RoundManager;
import eu.motogymkhana.competition.round.impl.RoundManagerImpl;
import eu.motogymkhana.competition.settings.SettingsManager;
import eu.motogymkhana.competition.settings.impl.SettingsManagerImpl;

public class GymkhanaModule extends AbstractModule {

	@Override
	protected void configure() {

		bind(Context.class).toProvider(CtxProvider.class);

		bind(RiderManager.class).to(RiderManagerImpl.class).asEagerSingleton();
		bind(RoundManager.class).to(RoundManagerImpl.class);
		bind(SettingsManager.class).to(SettingsManagerImpl.class);
		bind(ChristinePreferences.class).to(ChristinePreferencesImpl.class).asEagerSingleton();
		bind(GymkhanaDatabaseHelper.class).toProvider(GymkhanaDatabaseHelperProvider.class);

		bind(ApiManager.class).to(ApiManagerImpl.class).asEagerSingleton();
		bind(ApiUrlHelper.class).to(ApiUrlHelperImpl.class).asEagerSingleton();

		// db
		bind(RiderDao.class).toProvider(RiderDaoProvider.class).asEagerSingleton();
		bind(RoundDao.class).toProvider(RoundDaoProvider.class).asEagerSingleton();
		bind(TimesDao.class).toProvider(TimesDaoProvider.class).asEagerSingleton();
		bind(CredentialDao.class).toProvider(CredentialDaoProvider.class).asEagerSingleton();
		bind(SettingsDao.class).toProvider(SettingsDaoProvider.class).asEagerSingleton();
	}
}
