package eu.motogymkhana.competition.dao.impl;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.dao.RegistrationDao;
import eu.motogymkhana.competition.dao.RiderDao;
import eu.motogymkhana.competition.dao.TimesDao;
import eu.motogymkhana.competition.db.GymkhanaDatabaseHelper;
import eu.motogymkhana.competition.model.Bib;
import eu.motogymkhana.competition.model.Country;
import eu.motogymkhana.competition.model.Registration;
import eu.motogymkhana.competition.model.Rider;
import eu.motogymkhana.competition.model.Times;

public class TimesDaoImpl extends BaseDaoImpl<Times, Integer> implements TimesDao {

    private static final boolean ASCENDING = true;
    private static final String LOGTAG = TimesDaoImpl.class.getSimpleName();
    private static final boolean UNREGISTERED = false;
    private static final boolean REGISTERED = true;

    public TimesDaoImpl(ConnectionSource connectionSource, Class<Times> dataClass) throws SQLException {
        super(connectionSource, Times.class);
    }

    @Override
    public void storeRiderTimes(Rider rider) throws SQLException {

        Iterator<Times> iterator = rider.getTimes().iterator();

        while (iterator.hasNext()) {
            Times times = iterator.next();
            if (Constants.country == times.getCountry() && Constants.season == times.getSeason()) {
                times.setRider(rider);
                store(times);
            }
        }
    }

    @Override
    public Times getTimesForRiderForDate(int rider_id, long date, Country country, int season) throws
            SQLException {

        List<Times> list = null;
        QueryBuilder<Times, Integer> statementBuilder = queryBuilder();

        statementBuilder.where()
                .eq(Times.RIDER, rider_id).and()
                .eq(Times.DATE, date).and()
                .eq(Times.COUNTRY, country).and()
                .eq(Times.SEASON, season);


        list = query(statementBuilder.prepare());

        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<Times> getTimesForDateAndBib(long date, Bib bib) throws SQLException {

        RiderDao riderDao = GymkhanaDatabaseHelper.getInstance().getDao(Rider.class);
        RegistrationDao registrationDao = GymkhanaDatabaseHelper.getInstance().getDao(Registration.class);

        QueryBuilder<Rider, Integer> riderQueryBuilder = riderDao.queryBuilder();

        QueryBuilder<Registration, Integer> registrationQueryBuilder = registrationDao.queryBuilder();
        Where<Registration, Integer> registrationWhere = registrationQueryBuilder.where();
        registrationWhere.eq(Registration.BIB, bib);

        riderQueryBuilder.join(registrationQueryBuilder);

        QueryBuilder<Times, Integer> timesQueryBuilder = queryBuilder();
        Where<Times, Integer> where = timesQueryBuilder.where();
        where.eq(Times.DATE, date).and()
                .eq(Times.REGISTERED, true).and()
                .eq(Times.COUNTRY, Constants.country).and()
                .eq(Times.SEASON, Constants.season);

        List<Times> result = timesQueryBuilder.join(riderQueryBuilder).query();

        return result;
    }

    @Override
    public List<Rider> getRiders(long date) throws SQLException {

        QueryBuilder<Times, Integer> statementBuilder = queryBuilder();

        Where<Times, Integer> where = statementBuilder.where();
        where.eq(Times.DATE, date).and()
                .eq(Times.COUNTRY, Constants.country).and()
                .eq(Times.SEASON, Constants.season);

        List<Rider> riders = getRiders(query(statementBuilder.prepare()));
        return riders;
    }

    @Override
    public void store(Times times) throws SQLException {

        Times existingTimes = getTimesForRiderForDate(times.getRider().get_id(), times.getDate(), times.getCountry(),
                times.getSeason());

        if (existingTimes != null) {
            times.set_id(existingTimes.get_id());
            update(times);
            return;
        }
        times.setTimeStamp(System.currentTimeMillis());
        create(times);

        RiderDao riderDao = GymkhanaDatabaseHelper.getInstance().getDao(Rider.class);
        riderDao.update(times.getRider());
    }

    @Override
    public void clear() throws SQLException {

        List<Rider> list = null;
        DeleteBuilder<Times, Integer> statementBuilder = deleteBuilder();

        statementBuilder.where()
                .eq(Times.COUNTRY, Constants.country).and()
                .eq(Times.SEASON, Constants.season);

        statementBuilder.delete();
    }

    @Override
    public List<Rider> getUnregisteredRiders(long date, Bib bib) throws SQLException {
        return getRiders(date, bib, UNREGISTERED);
    }

    @Override
    public List<Rider> getRegisteredRiders(long date, Bib bib) throws SQLException {
        return getRiders(date, bib, REGISTERED);
    }

    private List<Rider> getRiders(long date, Bib bib, boolean registered) throws SQLException {

        RegistrationDao registrationDao = GymkhanaDatabaseHelper.getInstance().getDao(Registration.class);
        RiderDao riderDao = GymkhanaDatabaseHelper.getInstance().getDao(Rider.class);

        QueryBuilder<Times, Integer> timesQueryBuilder = queryBuilder();
        timesQueryBuilder.where().eq(Times.REGISTERED, registered).and()
                .eq(Times.DATE, date).and()
                .eq(Times.COUNTRY, Constants.country).and()
                .eq(Times.SEASON, Constants.season);

        List<Times> timesList = timesQueryBuilder.query();

        List<Rider> riders = new ArrayList<Rider>();
        for (Times times : timesList) {
            if (times != null && times.getRider() != null && times.getRider().getRegistration(Constants.country, Constants.season) != null) {
                Registration registration = times.getRider().getRegistration(Constants.country, Constants.season);
                if (registration != null && registration.getBib() == bib) {
                    riders.add(times.getRider());
                }
            }
        }

        QueryBuilder<Rider, Integer> riderQueryBuilder = riderDao.queryBuilder();

        QueryBuilder<Registration, Integer> registrationQueryBuilder = registrationDao.queryBuilder();
        registrationQueryBuilder.where().eq(Registration.BIB, bib);

        riderQueryBuilder.join(registrationQueryBuilder).join(timesQueryBuilder);

        List<Rider> result = riderQueryBuilder.query();

        return result;
    }

    @Override
    public List<Rider> getRegisteredRiders(long date) throws SQLException {

        QueryBuilder<Times, Integer> statementBuilder = queryBuilder();

        statementBuilder.where()
                .eq(Times.REGISTERED, true).and()
                .eq(Times.DATE, date).and()
                .eq(Times.COUNTRY, Constants.country).and()
                .eq(Times.SEASON, Constants.season);

        return getRiders(query(statementBuilder.prepare()));
    }

    private List<Rider> getRiders(List<Times> timesList) {

        List<Rider> riders = new ArrayList<Rider>();

        for (Times times : timesList) {
            if (times.hasRider()) {
                riders.add(times.getRider());
            }
        }

        return riders;
    }

    @Override
    public void delete(Country country, int season) throws SQLException {

        DeleteBuilder<Times, Integer> statementBuilder = deleteBuilder();
        statementBuilder.where().eq(Times.COUNTRY, country).and().eq(Times.SEASON, season);
        statementBuilder.delete();
    }
}
