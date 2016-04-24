package eu.motogymkhana.competition.dao.impl;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.dao.CredentialDao;
import eu.motogymkhana.competition.model.Credential;

/**
 * Created by christine on 14-2-16.
 */
public class CredentialDaoImpl extends BaseDaoImpl<Credential, Integer> implements CredentialDao {

    public CredentialDaoImpl(ConnectionSource connectionSource, Class<Credential> dataClass) throws SQLException {
        super(connectionSource, Credential.class);
    }


    @Override
    public Credential get() throws SQLException {

        List<Credential> list = null;
        QueryBuilder<Credential, Integer> statementBuilder = queryBuilder();

        statementBuilder.where()
                .eq(Credential.COUNTRY, Constants.country);

        list = query(statementBuilder.prepare());

        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public void store(Credential credential) throws SQLException {

        Credential existingCredential = get();

        if (existingCredential != null) {
            credential.set_id(existingCredential.get_id());
        }

        createOrUpdate(credential);
    }

    @Override
    public void setAdmin(boolean b) {

        try {
            Credential credential = get();
            if(credential == null){
                credential = new Credential();
                credential.setCountry(Constants.country);
            }

            credential.setAdmin(b);
            createOrUpdate(credential);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isAdmin(){

        try {
            Credential credential = get();
            return credential !=null && credential.isAdmin();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}