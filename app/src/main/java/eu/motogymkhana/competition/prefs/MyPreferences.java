package eu.motogymkhana.competition.prefs;

import eu.motogymkhana.competition.model.Country;

public interface MyPreferences {

    static final String PREFS_DEFAULT_FILENAME = "default.xml";

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

    boolean loadRounds();
}
