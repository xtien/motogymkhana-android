package eu.motogymkhana.competition.prefs.impl;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.prefs.ChristinePreferences;

@Singleton
public class ChristinePreferencesImpl implements ChristinePreferences {

    @Inject
    private SharedPreferences prefs;
    private String defaultDate = "2015-05-24";

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
    public void setAdmin(boolean b) {
        set(ADMIN, b);
    }

    @Override
    public boolean isAdmin() {
        return prefs.getBoolean(ADMIN, false);
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
        return prefs.getString(SERVER, Constants.hostName);
    }

    @Override
    public void setServer(String server) {
        set(SERVER, server);
    }

    @Override
    public int getPort() {
        return prefs.getInt(PORT, Constants.HTTP_PORT);
    }

    @Override
    public void setPort(int port) {
        set(PORT, port);
    }

    @Override
    public String getPassword() {
        return prefs.getString(PASSWORD, null);
    }

    @Override
    public void setPassword(String password) {
        set(PASSWORD, password);
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
