package eu.motogymkhana.competition.dao.impl;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.dao.RegistrationDao;
import eu.motogymkhana.competition.dao.RiderDao;
import eu.motogymkhana.competition.dao.TimesDao;
import eu.motogymkhana.competition.db.GymkhanaDatabaseHelper;
import eu.motogymkhana.competition.model.Registration;
import eu.motogymkhana.competition.model.Rider;
import eu.motogymkhana.competition.model.Times;

public class RiderDaoImpl extends BaseDaoImpl<Rider, Integer> implements RiderDao {

    private static final String LOGTAG = RiderDaoImpl.class.getSimpleName();

    public RiderDaoImpl(ConnectionSource connectionSource, Class<Rider> dataClass) throws SQLException {
        super(connectionSource, Rider.class);
    }

    @Override
    public Rider store(Rider rider) throws SQLException {

//        Log.d(LOGTAG, "store rider " + rider.getFullName());
        GymkhanaDatabaseHelper databaseHelper = GymkhanaDatabaseHelper.getInstance();

        TimesDao timesDao = databaseHelper.getDao(Times.class);
        RegistrationDao registrationDao = databaseHelper.getDao(Registration.class);

        String serverRiderId = rider.getRiderId();

        if (serverRiderId == null) {
            int createRiderResult = create(rider);
            if (createRiderResult < 1) {
                throw new IllegalStateException("createRiderResult should not be < 1 ");
            }
            rider.setAndroidId(Integer.toString(rider.get_id()));

        } else {
            rider.setTimeStamp(System.currentTimeMillis());
            int updateRiderResult = -1;

            Rider existingRider = getRiderByServerId(rider.getRiderId());
            if (existingRider == null && rider.hasAndroidId()) {
                existingRider = getRiderById(Integer.parseInt(rider.getAndroidId()));
            }

            if (existingRider == null) {
                updateRiderResult = create(rider);
            } else {
                existingRider.setAndroidId(Integer.toString(rider.get_id()));
                existingRider.setRiderId(serverRiderId);
                rider.set_id(existingRider.get_id());
                rider.setRiderId(existingRider.getRiderId());
                updateRiderResult = update(rider);
            }

            if (updateRiderResult < 1) {
                throw new IllegalStateException("updateRiderResult should not be < 1 ");
            }
        }

        timesDao.storeRiderTimes(rider);
        registrationDao.storeRiderRegistrations(rider);

        return rider;
    }

    @Override
    public Rider getRiderById(int id) throws SQLException {

        List<Rider> list = null;
        QueryBuilder<Rider, Integer> statementBuilder = queryBuilder();

        statementBuilder.where().eq(Rider.ID, id);
        list = query(statementBuilder.prepare());

        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public Rider getRiderByServerId(String serverRiderId) throws SQLException {

        List<Rider> list = null;
        QueryBuilder<Rider, Integer> statementBuilder = queryBuilder();

        statementBuilder.where().eq(Rider.SERVER_ID, serverRiderId);
        list = query(statementBuilder.prepare());

        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<Rider> queryForAllNonDayRider() throws SQLException {

        RegistrationDao registrationDao = GymkhanaDatabaseHelper.getInstance().getDao(Registration.class);

        QueryBuilder<Registration, Integer> registrationQueryBuilder = registrationDao.queryBuilder();
        Where<Registration, Integer> registrationWhere = registrationQueryBuilder.where();
        registrationWhere.eq(Registration.COUNTRY, Constants.country).and().eq(Registration.SEASON, Constants.season)
                .and().eq(Registration.DAY_RIDER, false);

        QueryBuilder<Rider, Integer> riderQueryBuilder = queryBuilder();
        riderQueryBuilder.join(registrationQueryBuilder);

        List<Rider> list = riderQueryBuilder.query();
        return list;
    }

    @Override
    public List<Rider> getRiders() throws SQLException {

        RegistrationDao registrationDao = GymkhanaDatabaseHelper.getInstance().getDao(Registration.class);

        QueryBuilder<Registration, Integer> registrationQueryBuilder = registrationDao.queryBuilder();
        Where<Registration, Integer> registrationWhere = registrationQueryBuilder.where();
        registrationWhere.eq(Registration.COUNTRY, Constants.country).and().eq(Registration.SEASON, Constants.season);

        QueryBuilder<Rider, Integer> riderQueryBuilder = queryBuilder();
        riderQueryBuilder.join(registrationQueryBuilder);

        List<Rider> list = riderQueryBuilder.query();

        return list;
    }

    @Override
    public List<Rider> getAllRiders() throws SQLException {

        QueryBuilder<Rider, Integer> riderQueryBuilder = queryBuilder();
        List<Rider> list = riderQueryBuilder.query();
        return list;
    }

    @Override
    public void store(Collection<Rider> riders) throws SQLException {

        Iterator<Rider> iterator = riders.iterator();

        while (iterator.hasNext()) {

            Rider r = iterator.next();
            store(r);
        }
    }

    @Override
    public void deleteRider(Rider rider) throws SQLException {

        GymkhanaDatabaseHelper databaseHelper = GymkhanaDatabaseHelper.getInstance();
        TimesDao timesDao = databaseHelper.getDao(Times.class);

        for (Times times : rider.getTimes()) {
            timesDao.delete(times);
        }
        delete(rider);
    }

    @Override
    public void clear() throws SQLException {

        List<Rider> list = null;
        DeleteBuilder<Rider, Integer> statementBuilder = deleteBuilder();

        statementBuilder.delete();
    }
}
