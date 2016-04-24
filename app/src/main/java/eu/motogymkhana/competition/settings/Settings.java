package eu.motogymkhana.competition.settings;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import eu.motogymkhana.competition.dao.impl.SettingsDaoImpl;
import eu.motogymkhana.competition.model.Country;

/**
 * Created by christine on 15-2-16.
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

    @JsonProperty(PERCENTAGE_FOR_GREEN_BIB)
    @DatabaseField(columnName = PERCENTAGE_FOR_GREEN_BIB)
    private int percentageForGreenBib = 115;

    @JsonProperty(PERCENTAGE_FOR_BLUE_BIB)
    @DatabaseField(columnName = PERCENTAGE_FOR_BLUE_BIB)
    private int percentageForBlueBib = 105;

    @JsonProperty(NUMBER_OF_RESULTS_FOR_SEASON_RESULT)
    @DatabaseField(columnName = NUMBER_OF_RESULTS_FOR_SEASON_RESULT)
    private int numberOfResultsForSeasonresult = 6;

    @JsonProperty(NUMBER_OF_RESULTS_FOR_BIB)
    @DatabaseField(columnName = NUMBER_OF_RESULTS_FOR_BIB)
    private int numberOfResultsForBib = 4;

    @JsonProperty(POINTS)
    @DatabaseField(columnName = POINTS)
    private String pointsList;

    @DatabaseField(columnName = HAS_ROUNDS)
    @JsonIgnore
    private boolean hasRounds = true;

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
