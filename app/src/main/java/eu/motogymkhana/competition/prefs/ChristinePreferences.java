package eu.motogymkhana.competition.prefs;

import eu.motogymkhana.competition.model.Country;

public interface ChristinePreferences {

    public static final String REGISTERED = "registered";
    public static final String INIT_APP = "init_app";
    public static final String LOGGED_IN = "logged_in";
    public static final String DATE = "date";
    public static final String RESET = "reset";
    public static final String FIRST_RUN = "first_run";
    public static final String RESULT_SORTED = "result_sorted";
    public static final String STARTUP = "startup";
    public static final String BLOCK_DOWNLOAD = "block_download";
    public static final String SERVER = "server";
    public static final String PORT = "port";
    public static final String VERSION_CODE = "version_code";
    public static final String COUNTRY = "country_code";
    public static final Country COUNTRY_DEFAULT = Country.NL;
    public static final String SEASON = "season";
    public static final int SEASON_DEFAULT = 2016;


    public long getDate();

    public void setDate(long dateString);

    public boolean isRegistered();

    public void setRegistered(boolean registered);

    public boolean initApp();

    public void setLoggedIn(boolean b);

    public boolean isLoggedIn();

    void setInitApp(boolean init);

    public boolean isReset();

    public void toggleReset();

    public boolean isFirstRun();

    public void setResultSorted(boolean randomSorted);

    boolean isResultSorted();

    public void setReset(boolean b);

    int startUpTimes();

    void setBlockDownload(boolean b);

    boolean isBlockDownload();

    String getServer();

    void setServer(String server);

    int getPort();

    void setPort(int port);

    void setVersionCode(int versionCode);

    int getVersionCode();

    Country getCountry();

    void setCountry(Country country);

    int getSeason();

    void setSeason(int season);
}
