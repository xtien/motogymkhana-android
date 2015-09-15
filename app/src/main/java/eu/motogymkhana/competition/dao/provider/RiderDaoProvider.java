package eu.motogymkhana.competition.dao.provider;

import com.google.inject.Inject;
import com.google.inject.Provider;

import java.sql.SQLException;

import eu.motogymkhana.competition.dao.RiderDao;
import eu.motogymkhana.competition.db.GymkhanaDatabaseHelper;
import eu.motogymkhana.competition.model.Rider;

public class RiderDaoProvider implements Provider<RiderDao> {

	@Inject
	private GymkhanaDatabaseHelper helper;

	@Override
	public RiderDao get() {

		RiderDao dao = null;

		try {
			dao = (RiderDao) helper.getDAO(Rider.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return dao;
	}

}
