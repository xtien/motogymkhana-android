package eu.motogymkhana.competition.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.text.ParseException;

import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.dao.impl.RoundDaoImpl;

@DatabaseTable(tableName = "rounds", daoClass = RoundDaoImpl.class)
public class Round {

    public static final String ID = "id";
    public static final String DATE = "date";
    public static final String TIMESTAMP = "timestamp";
    public static final String NUMBER = "number";
    public static final String CURRENT = "current";
    public static final String COUNTRY = "country";

    @DatabaseField(generatedId = true, columnName = ID)
    private int _id;

    @JsonProperty(DATE)
    @DatabaseField(columnName = DATE)
    private long date = 0l;

    @JsonProperty(NUMBER)
    @DatabaseField(columnName = NUMBER)
    private int number = 0;

    @JsonProperty(TIMESTAMP)
    @DatabaseField(columnName = TIMESTAMP)
    private long timeStamp = 0l;

    @JsonProperty(CURRENT)
    @DatabaseField(columnName = CURRENT)
    private boolean current = false;

    @JsonProperty(COUNTRY)
    @DatabaseField(columnName = COUNTRY)
    private Country country = Country.NL;

    public Round() {
        this.timeStamp = System.currentTimeMillis();
    }

    public Round(int nr, long date) throws ParseException {
        this.number = nr;
        this.date = date;
        this.timeStamp = System.currentTimeMillis();
    }

    public boolean isCurrent() {
        return current;
    }

    public long getDate() {
        return date;
    }

    @JsonIgnore
    public String getDateString(){
        return Constants.dateFormat.format(date);
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int id) {
        this._id = id;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setCurrent(boolean current) {

        if (this.current != current) {
            timeStamp = System.currentTimeMillis();
            this.current = current;
        }
    }

    public int getNumber(){
        return number;
    }
}
