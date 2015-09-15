package eu.motogymkhana.competition.dao.impl;

import com.google.inject.Singleton;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import eu.motogymkhana.competition.dao.RiderDao;
import eu.motogymkhana.competition.dao.TimesDao;
import eu.motogymkhana.competition.db.GymkhanaDatabaseHelper;
import eu.motogymkhana.competition.model.Rider;
import eu.motogymkhana.competition.model.Times;

@Singleton
public class RiderDaoImpl extends BaseDaoImpl<Rider, Integer> implements RiderDao {

    public RiderDaoImpl(ConnectionSource connectionSource, Class<Rider> dataClass) throws SQLException {
        super(connectionSource, Rider.class);
    }

    @Override
    public Rider store(Rider rider) throws SQLException {

        GymkhanaDatabaseHelper databaseHelper = GymkhanaDatabaseHelper.getInstance();

        TimesDao timesDao = databaseHelper.getDao(Times.class);

        Rider existingRider = getRiderByNumber(rider.getRiderNumber());

        if (existingRider == null) {

            create(rider);

        } else {
            rider.set_id(existingRider.get_id());
            rider.setTimeStamp(System.currentTimeMillis());
            update(rider);
        }

        timesDao.storeRiderTimes(rider);

        return rider;
    }

    @Override
    public Rider getRiderByNumber(int riderNumber) throws SQLException {

        List<Rider> list = null;
        QueryBuilder<Rider, Integer> statementBuilder = queryBuilder();

        statementBuilder.where().eq(Rider.RIDER_NUMBER, riderNumber);
        list = query(statementBuilder.prepare());

        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<Rider> queryForAllNonDayRider() throws SQLException {

        List<Rider> list = null;
        QueryBuilder<Rider, Integer> statementBuilder = queryBuilder();

        statementBuilder.where().eq(Rider.DAY_RIDER, false);
        list = query(statementBuilder.prepare());

        return list;
    }

    @Override
    public List<Rider> getRiders() throws SQLException {

        List<Rider> list = null;
        QueryBuilder<Rider, Integer> statementBuilder = queryBuilder();
        list = query(statementBuilder.prepare());

        return list;
    }

    @Override
    public void store(Collection<Rider> riders) throws SQLException {

        Collection<Rider> oldRiders = getRiders();

        Iterator<Rider> iterator = riders.iterator();

        while (iterator.hasNext()) {
            Rider r = iterator.next();
            store(r);
            if (oldRiders.contains(r)) {
                oldRiders.remove(r);
            }
        }

        if (oldRiders.size() > 0) {
            Iterator<Rider> oldIterator = oldRiders.iterator();
            while (oldIterator.hasNext()) {
                deleteRider(oldIterator.next());
            }
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
