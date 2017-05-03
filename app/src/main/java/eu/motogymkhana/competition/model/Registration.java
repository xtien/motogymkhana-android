/*
 * Copyright (c) 2015 - 2017, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import eu.motogymkhana.competition.dao.impl.RegistrationDaoImpl;

/**
 * Created by christine on 19-3-17.
 */
@DatabaseTable(tableName = "registrations", daoClass = RegistrationDaoImpl.class)
public class Registration {

    public static final String ID = "_id";
    public static final String SEASON = "season";
    public static final String COUNTRY = "country";
    public static final String NUMBER = "number";
    public static final String REGISTERED = "registered";
    public static final String RIDER = "rider_id";
    public static final String BIB = "bib";
    public static final String DAY_RIDER = "day_rider";

    @DatabaseField(generatedId = true, columnName = ID)
    @JsonIgnore
    protected int _id;

    @JsonProperty(SEASON)
    @DatabaseField(columnName = SEASON)
    protected int season;

    @JsonProperty(COUNTRY)
    @DatabaseField(columnName = COUNTRY)
    protected Country country;

    @JsonProperty(NUMBER)
    @DatabaseField(columnName = NUMBER)
    protected int number;

    @JsonProperty(BIB)
    @DatabaseField(columnName = BIB)
    protected Bib bib;

    @JsonProperty(REGISTERED)
    @DatabaseField(columnName = REGISTERED)
    protected boolean registered;

    @JsonProperty(DAY_RIDER)
    @DatabaseField(columnName = DAY_RIDER)
    protected boolean dayRider;

    @DatabaseField(foreign = true, columnName = RIDER, foreignAutoRefresh = true)
    @JsonIgnore
    private Rider rider;

    public Registration() {
    }

    public Registration(Country country, int season, int startNumber) {
        this.country = country;
        this.season = season;
        this.number = startNumber;
        this.bib = Bib.Y;
        registered = true;
    }

    public Registration(Country country, int season, boolean registered) {
        this.country = country;
        this.season = season;
        this.bib = Bib.Y;
        this.registered = registered;
    }

    public void set_id(int id){
        this._id = id;
    }

    public int get_id(){
        return _id;
    }

    public Country getCountry() {
        return country;
    }

    public int getSeason() {
        return season;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRider(Rider rider) {
        this.rider = rider;
    }

    public Bib getBib() {
        return bib;
    }

    public void setBib(Bib bib) {
        this.bib = bib;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
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
        Registration other = (Registration) obj;
        boolean result = new EqualsBuilder()
                .append(country, other.getCountry())
                .append(season, other.getSeason())
                //TODO
                .isEquals();

        return result;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(season)
                .append(country)
                .toHashCode();
    }

    public Rider getRider() {
        return rider;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean hasBib() {
        return bib != null;
    }
}
