package eu.motogymkhana.competition.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.dao.impl.RiderDaoImpl;
import eu.motogymkhana.competition.round.RoundManagerProvider;

@DatabaseTable(tableName = "riders", daoClass = RiderDaoImpl.class)
public class Rider {

    public static final String DATE_OF_BIRTH = "dob";
    public static final String GENDER = "gender";
    public static final String FIRSTNAME = "firstname";
    public static final String LASTNAME = "lastname";
    public static final String RIDER_NUMBER = "number";
    public static final String DAY_RIDER = "day_rider";
    public static final String ID = "_id";
    public static final String START_NUMBER = "startnumber";
    public static final String TIME1 = "time1";
    public static final String TIME2 = "time2";
    public static final String BEST_TIME = "best_time";
    public static final String REGISTERED = "registered";
    public static final String COUNTRY = "country";
    public static final String BIB = "bib";
    public static final String TEXT = "text";
    public static final String TIMES = "times";
    public static final String SHARING = "sharing";
    public static final String SEASON = "season";
    public static final String TIMESTAMP = "timestamp";
    public static final String NATIONALITY = "nationality";
    public static final String BIKE = "bike";
    public static final String IMAGE_URL = "image_url";
    public static final String BIKE_IMAGE_URL = "bike_image_url";
    public static final String EMAIL = "email";

    @DatabaseField(generatedId = true, columnName = ID)
    private int _id;

    @JsonProperty(SEASON)
    @DatabaseField(columnName = SEASON)
    private int season;

    @JsonProperty(COUNTRY)
    @DatabaseField(columnName = COUNTRY)
    private Country country;

    @JsonProperty(NATIONALITY)
    @DatabaseField(columnName = NATIONALITY)
    private Country nationality;

    @JsonProperty(BIKE)
    @DatabaseField(columnName = BIKE)
    private String bike;

    @JsonProperty(IMAGE_URL)
    @DatabaseField(columnName = IMAGE_URL)
    private String imageUrl;

    @JsonProperty(BIKE_IMAGE_URL)
    @DatabaseField(columnName = BIKE_IMAGE_URL)
    private String bikeImageUrl;

    @JsonProperty(TIMESTAMP)
    @DatabaseField(columnName = TIMESTAMP)
    private long timeStamp;

    @JsonProperty(FIRSTNAME)
    @DatabaseField(columnName = FIRSTNAME)
    private String firstName;

    @JsonProperty(LASTNAME)
    @DatabaseField(columnName = LASTNAME)
    private String lastName;

    @JsonProperty(EMAIL)
    @DatabaseField(columnName = EMAIL)
    private String email;

    @JsonProperty(RIDER_NUMBER)
    @DatabaseField(columnName = RIDER_NUMBER)
    private int riderNumber;

    @JsonProperty(DAY_RIDER)
    @DatabaseField(columnName = DAY_RIDER)
    private boolean dayRider;

    @JsonProperty(GENDER)
    @DatabaseField(columnName = GENDER)
    private Gender gender;

    @JsonProperty(DATE_OF_BIRTH)
    @DatabaseField(columnName = DATE_OF_BIRTH)
    private String dateOfBirth;

    @JsonProperty(BIB)
    @DatabaseField(columnName = BIB)
    private Bib bib = Bib.Y;

    @JsonProperty(TEXT)
    @DatabaseField(columnName = TEXT)
    private String text;

    @JsonProperty(TIMES)
    @ForeignCollectionField(eager = true, maxEagerLevel = 2)
    private Collection<Times> timesList = new LinkedList<Times>();

    @JsonIgnore
    @DatabaseField(columnName = SHARING)
    private int sharing;

    @DatabaseField(persisted = false)
    private int totalPoints;

    public Rider() {

    }

    public Rider(String riderString) {

        if (riderString.length() > 0) {

            riderString = riderString.replaceAll("\t", " ");
            riderString = riderString.replaceAll("  ", " ");
            String[] splitString = riderString.split(",");

            if (splitString.length > 0) {
                firstName = splitString[1];

                if (splitString.length > 1) {
                    lastName = splitString[2];
                }
            }

            String sex = splitString[3].substring(0, 1);
            gender = Gender.valueOf(sex);

            riderNumber = Integer.parseInt(splitString[0]);

            if (splitString.length > 4) {
                dateOfBirth = splitString[4];
            }
        }
    }

    public Rider(int number, String firstName, String lastName, Gender gender, Country country) {
        riderNumber = number;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.country = country;
        Times t = new Times();
        t.setRider(this);
        t.setRegistered(true);
        timesList.add(t);
    }

    @JsonIgnore
    public String getName() {
        return firstName + " " + lastName;
    }

    public int getRiderNumber() {
        return riderNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    @JsonIgnore
    public String getRiderNumberString() {
        return Integer.toString(riderNumber);
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int id) {
        this._id = id;
    }

    public void addTimes(Times times) {
        timesList.add(times);
    }

    @JsonIgnore
    public int getTotalPoints(int roundsCountingForSeasonResult) {

        List<Integer> totalPointsList = new ArrayList<Integer>();

        for (Times times : timesList) {
            totalPointsList.add(times.getPoints());
        }

        Collections.sort(totalPointsList);

        while (totalPointsList.size() > roundsCountingForSeasonResult) {
            totalPointsList.remove(0);
        }

        totalPoints = 0;

        for (Integer i : totalPointsList) {
            totalPoints += i;
        }

        return totalPoints;
    }

    @JsonIgnore
    public String getTotalPointsString(int roundsCountingForSeasonResult) {
        return Integer.toString(getTotalPoints(roundsCountingForSeasonResult));
    }

    public boolean hasTimes(long date) {
        Times t = getEUTimes(date);
        return t != null && !(t.isDisqualified1() && t.isDisqualified2()) && t.getBestTime() > 0;
    }

    public boolean hasTimes() {
        return timesList != null && timesList.size() > 0;
    }

    @JsonIgnore
    public int getFirstTimeForSort(long date) {

        Times times = getEUTimes(date);

        return times.getPenalties1() + (times.getTime1() != 0 ? times.getTime1() : 360000);
    }

    @Override
    public String toString() {
        return getName();
    }

    public boolean isDayRider() {
        return dayRider;
    }

    public void setDayRider(boolean dayRider) {
        this.dayRider = dayRider;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    @JsonIgnore
    public boolean isValid() {
        return firstName != null && firstName.length() > 0 && lastName != null && lastName.length() > 0 &&
                riderNumber > 0;
    }

    public Gender getGender() {
        return gender;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @JsonIgnore
    public int getBestTime(long date) {

        Times times = getEUTimes(date);

        if (times != null) {
            return times.getBestTime();
        } else {
            return 0;
        }
    }

    @JsonIgnore
    public int getBestTime() {

        Times times = getEUTimes(RoundManagerProvider.getInstance().getDate());

        if (times != null) {
            return times.getBestTime();
        } else {
            return 0;
        }
    }

    @JsonIgnore
    public Times getEUTimes(long date) {

        if (date != 0l) {
            Iterator<Times> iterator = timesList.iterator();
            while (iterator.hasNext()) {
                Times times = iterator.next();

                if (times.isDate(date)) {
                    return times;
                }
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object other) {

        if (!(other instanceof Rider)) {
            return false;
        }

        Rider otherRider = (Rider) other;
        return otherRider.getRiderNumber() == riderNumber;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(riderNumber).append(firstName).append(lastName).toHashCode();
    }

    public void setRiderNumber(int number) {
        this.riderNumber = number;
    }

    @JsonIgnore
    public Collection<Times> getTimes() {
        return timesList;
    }

    public boolean newerThan(Rider r) {
        return timeStamp > r.getTimeStamp();
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public void setBib(Bib bib) {
        this.bib = bib;
    }

    public Bib getBib() {

        if (bib == null) {
            return Bib.Y;
        } else {
            return bib;
        }
    }

    @JsonIgnore
    public int getStartNumber() {
        return getEUTimes(RoundManagerProvider.getInstance().getDate()).getStartNumber();
    }

    public int getSharing() {
        return sharing;
    }

    public void setSharing(int sharing) {
        this.sharing = sharing;
    }

    public void setBike(String bike) {
        this.bike = bike;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getBike() {
        return bike;
    }

    public String getText() {
        return text;
    }

    public boolean hasImageUrl() {
        return imageUrl != null;
    }

    public boolean hasBikeImageUrl() {
        return bikeImageUrl != null;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getBikeImageUrl() {
        return bikeImageUrl;
    }

    public Country getNationality() {
        return nationality;
    }

    public void setNationality(Country nationality) {
        this.nationality = nationality;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public void clearTimes() {
        timesList.clear();
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
