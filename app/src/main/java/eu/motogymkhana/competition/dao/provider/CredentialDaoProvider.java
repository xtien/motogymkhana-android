package eu.motogymkhana.competition.dao.provider;

import com.google.inject.Inject;
import com.google.inject.Provider;

import java.sql.SQLException;

import eu.motogymkhana.competition.dao.CredentialDao;
import eu.motogymkhana.competition.db.GymkhanaDatabaseHelper;
import eu.motogymkhana.competition.model.Credential;

public class CredentialDaoProvider implements Provider<CredentialDao> {

	@Inject
	private GymkhanaDatabaseHelper helper;

	@Override
	public CredentialDao get() {

		CredentialDao dao = null;

		try {
			dao = (CredentialDao) helper.getDAO(Credential.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return dao;
	}
}
