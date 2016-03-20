package eu.motogymkhana.competition.dao.provider;

import com.google.inject.Inject;
import com.google.inject.Provider;

import java.sql.SQLException;

import eu.motogymkhana.competition.dao.CredentialDao;
import eu.motogymkhana.competition.dao.SettingsDao;
import eu.motogymkhana.competition.db.GymkhanaDatabaseHelper;
import eu.motogymkhana.competition.model.Credential;
import eu.motogymkhana.competition.settings.Settings;

public class SettingsDaoProvider implements Provider<SettingsDao> {

	@Inject
	private GymkhanaDatabaseHelper helper;

	@Override
	public SettingsDao get() {

		SettingsDao dao = null;

		try {
			dao = (SettingsDao) helper.getDAO(Settings.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return dao;
	}
}
