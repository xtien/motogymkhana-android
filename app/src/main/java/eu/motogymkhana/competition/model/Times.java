/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.dao.impl.TimesDaoImpl;

/**
 * created by Christine
 * A rider has a times object for every round they participate in. Registration is registered per times object.
 * times and penalty points are registered in a times object.
 */
@DatabaseTable(tableName = "times", daoClass = TimesDaoImpl.class)
public class Times {

    private static final String LOGTAG = Times.class.getSimpleName();

    public static final String ID = "_id";
    public static final String START_NUMBER = "startnumber";
    public static final String TIME1 = "time1";
    public static final String TIME2 = "time2";
    public static final String REGISTERED = "registered";
    public static final String DATE = "date";
    public static final String SEASON = "season";
    public static final String COUNTRY = "country";

    public static final String RIDER = "rider_id";
    private static final String PENALTIES1 = "pen1";
    private static final String PENALTIES2 = "pen2";
    private static final String DISQUALIFIED1 = "dis1";
    private static final String DISQUALIFIED2 = "dis2";
    private static final String TIMESTAMP = "timestamp";

    @DatabaseField(generatedId = true, columnName = ID)
    @JsonIgnore
    private int _id;

    @JsonProperty(SEASON)
    @DatabaseField(columnName = SEASON)
    private int season;

    @JsonProperty(COUNTRY)
    @DatabaseField(columnName = COUNTRY)
    private Country country;

    @JsonProperty(TIMESTAMP)
    @DatabaseField(columnName = TIMESTAMP)
    private long timeStamp;

    @JsonProperty(DATE)
    @DatabaseField(columnName = DATE)
    private long date;

    @JsonProperty(TIME1)
    @DatabaseField(columnName = TIME1)
    private int time1;

    @JsonProperty(TIME2)
    @DatabaseField(columnName = TIME2)
    private int time2;

    @JsonProperty(PENALTIES1)
    @DatabaseField(columnName = PENALTIES1)
    private int penalties1 = 0;

    @JsonProperty(PENALTIES2)
    @DatabaseField(columnName = PENALTIES2)
    private int penalties2 = 0;

    @JsonProperty(DISQUALIFIED1)
    @DatabaseField(columnName = DISQUALIFIED1)
    private boolean disqualified1;

    @JsonProperty(DISQUALIFIED2)
    @DatabaseField(columnName = DISQUALIFIED2)
    private boolean disqualified2;

    @JsonProperty(REGISTERED)
    @DatabaseField(columnName = REGISTERED)
    private boolean registered = false;

    @DatabaseField(persisted = false)
    private int points = 0;

    @DatabaseField(columnName = START_NUMBER)
    @JsonProperty(START_NUMBER)
    private int startNumber;

    @JsonIgnore
    @DatabaseField(foreign = true, columnName = RIDER, foreignAutoRefresh = true)
    private Rider rider;

    @DatabaseField(persisted = false)
    @JsonProperty(Rider.RIDER_NUMBER)
    private int riderNumber;

    public Times() {
        timeStamp = System.currentTimeMillis();
        country = Constants.country;
        season = Constants.season;
    }

    public Times(long date) {
        timeStamp = System.currentTimeMillis();
        this.date = date;
        country = Constants.country;
        season = Constants.season;
    }

    public int get_id() {
        return _id;
    }

    public int getTime1() {
        return time1;
    }

    public int getTime2() {
        return time2;
    }

    @JsonIgnore
    public void setTime1(int milliseconds) {
        timeStamp = System.currentTimeMillis();
        time1 = milliseconds;
    }

    @JsonIgnore
    public void setTime2(int milliseconds) {
        timeStamp = System.currentTimeMillis();
        time2 = milliseconds;
    }

    @JsonIgnore
    public int getBestTime() {

        int bestTime = 0;

        int t1 = time1 + (1000 * penalties1);
        int t2 = time2 + (1000 * penalties2);

        if (!disqualified2 && t2 != 0 && (t2 < t1 || t1 == 0 || disqualified1)) {
            bestTime = t2;

        } else if (!disqualified1) {
            bestTime = t1;

        } else {
            bestTime = 0;
        }

        return bestTime;
    }

    @JsonIgnore
    public void setTime1(String string) {
        timeStamp = System.currentTimeMillis();
        setTime1(convertTimeString(string));
    }

    @JsonIgnore
    public void setTime2(String string) {
        timeStamp = System.currentTimeMillis();
        setTime2(convertTimeString(string));
    }

    @JsonIgnore
    public String getTime1PlusPenaltiesString() {
        return makeString(time1 + (penalties1 * 1000));
    }

    @JsonIgnore
    public String getTime2PlusPenaltiesString() {
        return makeString(time2 + (penalties2 * 1000));
    }

    @JsonIgnore
    public String getTime1String() {
        return makeString(time1);
    }

    @JsonIgnore
    public String getTime2String() {
        return makeString(time2);
    }

    @JsonIgnore
    public CharSequence getBestTimeString() {
        return makeString(getBestTime());
    }

    @JsonIgnore
    public String getStartNumberString() {
        return Integer.toString(startNumber);
    }

    private String makeString(int time) {

        int minutes = time / 60000;
        int milliseconds = time - (minutes * 60000);
        int seconds = milliseconds / 1000;
        int centiseconds = (milliseconds - (seconds * 1000)) / 10;

        String secondsString = Integer.toString(seconds);
        String centiSecondsString = Integer.toString(centiseconds);

        return Integer.toString(minutes) + ":" + (secondsString.length() == 1 ? "0" : "") + secondsString + "." +
                (centiSecondsString.length() == 1 ? "0" : "") + centiSecondsString;
    }

    private int convertTimeString(String string) {

        if (string.length() == 0) {
            return 0;
        } else {

            int time = 0;

            String stringMinutes = "";
            String stringSeconds = "";
            String stringHundredths = "";

            if (StringUtils.isNumeric(string)) {

                if (string.length() == 1) {
                    stringHundredths = string;
                } else if (string.length() > 1) {
                    stringHundredths = string.substring(string.length() - 2, string.length());
                }

                if (string.length() == 3) {
                    stringSeconds = string.substring(string.length() - 3, string.length() - 2);
                } else if (string.length() > 3) {
                    stringSeconds = string.substring(string.length() - 4, string.length() - 2);
                }

                if (string.length() > 4) {
                    stringMinutes = string.substring(0, string.length() - 4);
                }
                if (stringMinutes.length() > 0) {
                    time = Integer.parseInt(stringMinutes) * 60000;
                }

                if (stringSeconds.length() > 0) {
                    time = time + Integer.parseInt(stringSeconds) * 1000;
                }

                if (stringHundredths.length() > 1) {
                    time = time + Integer.parseInt(stringHundredths) * 10;
                }

            } else if (string.contains(":") && string.contains(".")) {

                String[] string1 = string.split(":");

                if (string1.length > 1) {
                    String[] string2 = string1[1].split("\\.");

                    time = Integer.parseInt(string1[0]) * 60000;

                    if (string2.length > 0) {
                        time = time + Integer.parseInt(string2[0]) * 1000;
                    }

                    if (string2.length > 1) {
                        time = time + Integer.parseInt(string2[1]) * 10;
                    }
                }
            }

            return time;
        }
    }

    public void setRegistered(boolean isChecked) {
        timeStamp = System.currentTimeMillis();
        registered = isChecked;
    }

    public boolean isRegistered() {
        return registered;
    }

    public long getDate() {
        return date;
    }

    public void setRider(eu.motogymkhana.competition.model.Rider rider) {
        this.rider = rider;
    }

    public Rider getRider() {
        return rider;
    }

    @JsonIgnore
    public String getPenalties1String() {
        return penalties1 == 0 ? "" : Integer.toString(penalties1);
    }

    @JsonIgnore
    public String getPenalties2String() {
        return penalties2 == 0 ? "" : Integer.toString(penalties2);
    }

    public void setPenalties1(int i) {
        timeStamp = System.currentTimeMillis();
        penalties1 = i;
    }

    public void setPenalties2(int i) {
        timeStamp = System.currentTimeMillis();
        penalties2 = i;
    }

    public void setDisqualified1(boolean checked) {
        timeStamp = System.currentTimeMillis();
        disqualified1 = checked;
    }

    public void setDisqualified2(boolean checked) {
        timeStamp = System.currentTimeMillis();
        disqualified2 = checked;
    }

    public boolean isDisqualified1() {
        return disqualified1;
    }

    public boolean isDisqualified2() {
        return disqualified2;
    }

    public int getPenalties1() {
        return penalties1;
    }

    public int getPenalties2() {
        return penalties2;
    }

    public void setPoints(int p) {
        points = p;
    }

    public int getPoints() {
        return points;
    }

    public int getStartNumber() {
        return startNumber;
    }

    public void setStartNumber(int startNumber) {
        timeStamp = System.currentTimeMillis();
        this.startNumber = startNumber;
    }

    public void setDate(long date) {
        this.date = date;
    }

    @JsonIgnore
    public boolean isDate(long date) {
        return this.date == date;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public void setRiderNumber(int riderNumber) {
        this.riderNumber = riderNumber;
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
        Times other = (Times) obj;
        boolean result = new EqualsBuilder()
                .append(date, other.getDate())
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

    public void setRiderNumber() {
        if (rider != null) {
            riderNumber = rider.getRiderNumber();
        }
    }

    public boolean hasRider() {
        return rider != null;
    }

    public boolean hasStartNumber() {
        return startNumber != 0;
    }

    public void addToStartNumber(int delta) {
        startNumber += delta;
    }

    public void clearStartNumber() {
        startNumber = 0;
    }

    public void merge(Times times) {
        this.startNumber = times.getStartNumber();
        this.time1 = times.getTime1();
        this.time2 = times.getTime2();
        this.penalties1 = times.getPenalties1();
        this.penalties2 = times.getPenalties2();
        this.disqualified1 = times.isDisqualified1();
        this.disqualified2 = times.isDisqualified2();
        this.registered = times.isRegistered();
        this.points = times.getPoints();
        this.timeStamp = times.getTimeStamp();
    }

    public void set_id(int id) {
        this._id = id;
    }

    public String toString() {
        return "" + _id + " " + country.name() + " " + season + " " + startNumber + " " +
                (rider == null ? "no rider" : rider.getFullName()) + " "
                + registered;
    }
}