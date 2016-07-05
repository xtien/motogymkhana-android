package eu.motogymkhana.competition.dao.impl;

import com.google.inject.Singleton;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.dao.RoundDao;
import eu.motogymkhana.competition.model.Country;
import eu.motogymkhana.competition.model.Round;

@Singleton
public class RoundDaoImpl extends BaseDaoImpl<Round, Integer> implements RoundDao {

    private final static String LOGTAG = RoundDaoImpl.class.getSimpleName();
    private static final boolean ASCENDING = true;

    public RoundDaoImpl(ConnectionSource connectionSource, Class<Round> dataClass) throws SQLException {
        super(connectionSource, Round.class);
    }

    @Override
    public Round store(Round round) throws SQLException {

        Round r = getRoundByDate(round.getDate());

        if (r == null) {
            create(round);
         } else {
            round.set_id(r.get_id());
            update(round);
        }

        return round;
    }

    @Override
    public Round getRoundByDate(long date) throws SQLException {

        List<Round> list = null;
        QueryBuilder<Round, Integer> statementBuilder = queryBuilder();

        statementBuilder.where()
                .eq(Round.DATE, date).and()
                .eq(Round.COUNTRY, Constants.country).and()
                .eq(Round.SEASON, Constants.season);
        list = query(statementBuilder.prepare());

        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<Round> getRounds() throws SQLException {

        List<Round> list = null;
        QueryBuilder<Round, Integer> statementBuilder = queryBuilder();

        statementBuilder.where()
                .eq(Round.COUNTRY, Constants.country).and()
                .eq(Round.SEASON, Constants.season);
        statementBuilder.orderBy(Round.DATE, ASCENDING);

        list = query(statementBuilder.prepare());

        return list;
    }

    @Override
    public Round getRoundByNumber(int number) throws SQLException {

        List<Round> list = null;
        QueryBuilder<Round, Integer> statementBuilder = queryBuilder();

        statementBuilder.where()
                .eq(Round.NUMBER, number).and()
                .eq(Round.COUNTRY, Constants.country).and()
                .eq(Round.SEASON, Constants.season);

        list = query(statementBuilder.prepare());

        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public void store(Collection<Round> rounds) throws SQLException {

        Collection<Round> existingRounds = getRounds();


        for (Round round : rounds) {
            store(round);
            if(existingRounds.contains(round)){
                existingRounds.remove(round);
            }
        }

        for(Round round : existingRounds){
            delete(round);
        }
    }

    @Override
    public Round getCurrentRound() throws SQLException {

        List<Round> list = null;
        QueryBuilder<Round, Integer> statementBuilder = queryBuilder();

        statementBuilder.where()
                .eq(Round.CURRENT, true).and()
                .eq(Round.COUNTRY, Constants.country).and()
                .eq(Round.SEASON, Constants.season);

        list = query(statementBuilder.prepare());

        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public void delete(Country country, int season) throws SQLException {

        DeleteBuilder<Round, Integer> statementBuilder = deleteBuilder();

        statementBuilder.where().eq(Round.COUNTRY, country).and().eq(Round.SEASON, season);

        statementBuilder.delete();
    }

    @Override
    public void remove(Round r) throws SQLException {
        delete(r);
    }

    @Override
    public long getCurrentDate() throws SQLException {
        Round round = getCurrentRound();
        if(round !=null){
            return round.getDate();
        } else {
            return 0l;
        }
    }
}
