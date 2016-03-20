package eu.motogymkhana.competition.dao;

import java.sql.SQLException;

import eu.motogymkhana.competition.model.Credential;

/**
 * Created by christine on 14-2-16.
 */
public interface CredentialDao {

    Credential get() throws SQLException;

    void store(Credential credential) throws SQLException;

    void setAdmin(boolean b);

    boolean isAdmin();
}
