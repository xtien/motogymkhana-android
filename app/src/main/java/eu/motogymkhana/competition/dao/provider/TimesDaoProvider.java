package eu.motogymkhana.competition.dao.provider;

import com.google.inject.Inject;
import com.google.inject.Provider;

import java.sql.SQLException;

import eu.motogymkhana.competition.dao.TimesDao;
import eu.motogymkhana.competition.db.GymkhanaDatabaseHelper;
import eu.motogymkhana.competition.model.Times;

public class TimesDaoProvider implements Provider<TimesDao> {

	@Inject
	private GymkhanaDatabaseHelper helper;

	@Override
	public TimesDao get() {

		TimesDao dao = null;

		try {
			dao = (TimesDao) helper.getDAO(Times.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return dao;
	}

}
