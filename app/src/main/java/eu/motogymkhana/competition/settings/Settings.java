/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.settings;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import eu.motogymkhana.competition.dao.impl.SettingsDaoImpl;
import eu.motogymkhana.competition.model.Country;

/**
 * Created by christine on 15-2-16.
 * Settings for a season of a competition. Settings can be updated per country per season. Settings are distributed
 * immediately and active immediately, which means a change of settings immediately affects the data in the app and
 * on the web site.
 */
@DatabaseTable(tableName = "settings", daoClass = SettingsDaoImpl.class)
public class Settings {

    public static final String ID = "_id";
    public static final String PERCENTAGE_FOR_GREEN_BIB = "percentage_for_green_bib";
    public static final String PERCENTAGE_FOR_BLUE_BIB = "percentage_for_blue_bib";
    public static final String NUMBER_OF_RESULTS_FOR_SEASON_RESULT = "number_of_results_for_season_result";
    public static final String NUMBER_OF_RESULTS_FOR_BIB = "number_of_results_for_bib";
    public static final String COUNTRY = "country";
    public static final String SEASON = "season";
    private static final String HAS_ROUNDS = "has_rounds";
    public static final String POINTS = "points";

    @JsonIgnore
    @DatabaseField(generatedId = true, columnName = ID)
    private int _id;

    @JsonProperty(COUNTRY)
    @DatabaseField(columnName = COUNTRY)
    private Country country;

    @JsonProperty(SEASON)
    @DatabaseField(columnName = SEASON)
    private int season;

    /**
     * when a riders result is within this percentage of the day's winner, they get
     * one point for a green bib.
     */
    @JsonProperty(PERCENTAGE_FOR_GREEN_BIB)
    @DatabaseField(columnName = PERCENTAGE_FOR_GREEN_BIB)
    private int percentageForGreenBib = 115;

    /**
     * when a riders result is within this percentage of the day's winner, they get
     * one point for a blue bib.
     */
    @JsonProperty(PERCENTAGE_FOR_BLUE_BIB)
    @DatabaseField(columnName = PERCENTAGE_FOR_BLUE_BIB)
    private int percentageForBlueBib = 105;

    /**
     * number of results that counts for season results. If this is 6, and the
     * number of rounds is 8, it means we have "best 6 out of 8"
     */
    @JsonProperty(NUMBER_OF_RESULTS_FOR_SEASON_RESULT)
    @DatabaseField(columnName = NUMBER_OF_RESULTS_FOR_SEASON_RESULT)
    private int numberOfResultsForSeasonresult = 6;

    /**
     * number of bib points a rider needs to get a new bib.
     */
    @JsonProperty(NUMBER_OF_RESULTS_FOR_BIB)
    @DatabaseField(columnName = NUMBER_OF_RESULTS_FOR_BIB)
    private int numberOfResultsForBib = 4;

    /**
     * A list with integers that are the number of points you get when you end as nth. List
     * starts with points for nr 1.
     */
    @JsonProperty(POINTS)
    @DatabaseField(columnName = POINTS)
    private String pointsList;

    /**
     * utility field
     */
    @DatabaseField(columnName = HAS_ROUNDS)
    @JsonIgnore
    private boolean hasRounds = true;

    /**
     * utility field
     */
    @DatabaseField(persisted = false)
    @JsonIgnore
    private int[] points;

    public int getPercentageBlue() {
        return percentageForBlueBib;
    }

    public int getPercentageGreen() {
        return percentageForGreenBib;
    }

    public int getRoundsForBib() {
        return numberOfResultsForBib;
    }

    public int getRoundsForSeasonResult() {
        return numberOfResultsForSeasonresult;
    }

    public void setPercentageBlue(int s) {
        percentageForBlueBib = s;
    }

    public void setPercentageGreen(int s) {
        percentageForGreenBib = s;
    }

    public void setNumberOfRoundsForBib(int i) {
        numberOfResultsForBib = i;
    }

    public void setNumberOfRoundsForSeasonResult(int i) {
        numberOfResultsForSeasonresult = i;
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

    public void setSeason(int season) {
        this.season = season;
    }

    public int getSeason() {
        return season;
    }

    public Country getCountry() {
        return country;
    }

    public void setHasRounds(boolean b) {
        hasRounds = b;
    }

    public boolean hasRounds() {
        return hasRounds;
    }

    public void setPoints(String string) {
        this.pointsList = string;
    }

    public int[] getPoints() {

        if (points == null && pointsList != null && pointsList.length() > 1) {
            String[] pts = pointsList.split(",");
            points = new int[pts.length];
            for (int i = 0; i < pts.length; i++) {
                points[i] = Integer.parseInt(pts[i]);
            }
        }
        return points;
    }

    public String getPointsString() {
        return pointsList;
    }
}
