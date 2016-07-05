package eu.motogymkhana.competition.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
    public static final String SEASON = "season";

    @DatabaseField(generatedId = true, columnName = ID)
    @JsonIgnore
    private int _id;

    @DatabaseField(columnName = SEASON)
    @JsonProperty(SEASON)
    private int season;

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
    private Country country;

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
    public String getDateString() {
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

    public int getNumber() {
        return number;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        Round other = (Round) obj;
        boolean result = new EqualsBuilder()
                .append(date, other.date)
                .append(country, other.getCountry())
                .append(season, other.getSeason())
                .isEquals();

        return result;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).
                append(date)
                .append(season)
                .append(country)
                .toHashCode();
    }

    public Country getCountry() {
        return country;
    }

    public int getSeason() {
        return season;
    }
}
