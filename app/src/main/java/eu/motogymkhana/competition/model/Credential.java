package eu.motogymkhana.competition.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import eu.motogymkhana.competition.dao.impl.CredentialDaoImpl;
import eu.motogymkhana.competition.dao.impl.RiderDaoImpl;

/**
 * Created by christine on 14-2-16.
 */
@DatabaseTable(tableName = "credentials", daoClass = CredentialDaoImpl.class)
public class Credential {

    public static final String ID = "_id";
    public static final String COUNTRY = "country";
    public static final String PW = "pw";
    private static final String ADMIN = "admin";

    @DatabaseField(generatedId = true, columnName = ID)
    private int _id;

    @DatabaseField(columnName = COUNTRY)
    private Country country;

    @DatabaseField(columnName = ADMIN)
    private boolean admin;

    @DatabaseField(columnName = PW)
    private String password;

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isAdmin(){
        return admin;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getPassword() {
        return password;
    }
}
