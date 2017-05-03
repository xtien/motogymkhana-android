package eu.motogymkhana.competition.prefs.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import javax.inject.Inject;
import javax.inject.Singleton;

import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.Server;
import eu.motogymkhana.competition.model.Country;
import eu.motogymkhana.competition.prefs.MyPreferences;

@Singleton
public class MyPreferencesImpl implements MyPreferences {

    private static final String REGISTERED = "registered";
    private static final String INIT_APP = "init_app";
    private static final String LOGGED_IN = "logged_in";
    private static final String DATE = "date";
    private static final String RESET = "reset";
    private static final String FIRST_RUN = "first_run";
    private static final String RESULT_SORTED = "result_sorted";
    private static final String STARTUP = "startup";
    private static final String BLOCK_DOWNLOAD = "block_download";
    private static final String SERVER = "server";
    private static final String PORT = "port";
    private static final String VERSION_CODE = "version_code";
    private static final String COUNTRY = "country_code";
    private static final Country COUNTRY_DEFAULT = Country.NL;
    private static final String SEASON = "season";
    private static final String LOAD_ROUNDS = "load_rounds";
    private static final int SEASON_DEFAULT = 0;

    private SharedPreferences prefs;

    @Inject
    public MyPreferencesImpl(Context context) {
        prefs = context.getSharedPreferences(PREFS_DEFAULT_FILENAME, Context.MODE_PRIVATE);
    }

    @Override
    public boolean isRegistered() {
        return prefs.getBoolean(REGISTERED, false);
    }

    @Override
    public void setRegistered(boolean registered) {
        set(REGISTERED, registered);
    }

    @Override
    public void setLoggedIn(boolean b) {
        set(LOGGED_IN, b);
    }

    @Override
    public boolean isLoggedIn() {
        return prefs.getBoolean(LOGGED_IN, false);
    }

    @Override
    public boolean isReset() {
        return prefs.getBoolean(RESET, false);
    }

    @Override
    public void setReset(boolean b) {
        set(RESET, b);
    }

    @Override
    public int startUpTimes() {

        int startup = prefs.getInt(STARTUP, 3);
        if (startup > 0) {
            set(STARTUP, startup - 1);
        }

        return startup;
    }

    @Override
    public void setBlockDownload(boolean b) {
        set(BLOCK_DOWNLOAD, b);
    }

    @Override
    public boolean isBlockDownload() {
        return prefs.getBoolean(BLOCK_DOWNLOAD, false);
    }

    @Override
    public String getServer() {
        return prefs.getString(SERVER, Server.hostName);
    }

    @Override
    public void setServer(String server) {
        set(SERVER, server);
    }

    @Override
    public int getPort() {
        return prefs.getInt(PORT, Server.HTTP_PORT);
    }

    @Override
    public void setPort(int port) {
        set(PORT, port);
    }

    @Override
    public void setVersionCode(int versionCode) {
        set(VERSION_CODE, versionCode);
    }

    @Override
    public int getVersionCode() {
        return prefs.getInt(VERSION_CODE, 0);
    }

    @Override
    public Country getCountry() {
        String countryString = prefs.getString(COUNTRY, null);
        if (countryString != null) {
            return Country.valueOf(countryString);
        } else {
            return null;
        }
    }

    @Override
    public void setCountry(Country country) {
        Constants.country = country;
        set(COUNTRY, country.name());
    }

    @Override
    public int getSeason() {
        return prefs.getInt(SEASON, SEASON_DEFAULT);
    }

    @Override
    public void setSeason(int season) {
        Constants.season = season;
        set(SEASON, season);
    }

    @Override
    public boolean loadRounds() {

        long roundsLoadedAt = prefs.getLong(LOAD_ROUNDS, 0l);
        if ((System.currentTimeMillis() - roundsLoadedAt) > 24 * 3600 * 1000l) {
            set(LOAD_ROUNDS, System.currentTimeMillis());
            return true;
        }
        return false;
    }

    @Override
    public void toggleReset() {
        set(RESET, !isReset());
    }

    @Override
    public boolean initApp() {
        return prefs.getBoolean(INIT_APP, false);
    }

    @Override
    public void setInitApp(boolean init) {
        set(INIT_APP, init);

    }

    @Override
    public void setDate(long date) {
        set(DATE, date);
    }

    @Override
    public long getDate() {
        return prefs.getLong(DATE, 0);
    }

    @Override
    public void setResultSorted(boolean sorted) {
        set(RESULT_SORTED, sorted);
    }

    @Override
    public boolean isResultSorted() {
        return prefs.getBoolean(RESULT_SORTED, false);
    }

    @Override
    public boolean isFirstRun() {
        final boolean firstRun = prefs.getBoolean(FIRST_RUN, true);

        if (firstRun) {
            set(FIRST_RUN, false);
        }

        return firstRun;
    }

    private void set(String key, Object value) {

        Editor editor = prefs.edit();
        if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        }
        editor.commit();
    }
}
