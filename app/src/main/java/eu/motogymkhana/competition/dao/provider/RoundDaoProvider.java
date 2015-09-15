package eu.motogymkhana.competition.dao.provider;

import com.google.inject.Inject;
import com.google.inject.Provider;

import java.sql.SQLException;

import eu.motogymkhana.competition.dao.RoundDao;
import eu.motogymkhana.competition.db.GymkhanaDatabaseHelper;
import eu.motogymkhana.competition.model.Round;

public class RoundDaoProvider implements Provider<RoundDao> {

	@Inject
	private GymkhanaDatabaseHelper helper;

	@Override
	public RoundDao get() {

		RoundDao dao = null;

		try {
			dao = (RoundDao) helper.getDAO(Round.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return dao;
	}

}
