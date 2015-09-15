package eu.motogymkhana.competition.prefs;

public interface ChristinePreferences {

	public static final String REGISTERED = "registered";
	public static final String INIT_APP = "init_app";
	public static final String LOGGED_IN = "logged_in";
	public static final String DATE = "date";
	public static final String RESET = "reset";
	public static final String FIRST_RUN = "first_run";
	public static final String SORTED = "sorted";
	public static final String RESULT_SORTED = "result_sorted";
	public static final String ADMIN = "admin";
	public static final String STARTUP = "startup";
	public static final String BLOCK_DOWNLOAD = "block_download";
	public static final String SERVER = "server";
	public static final String PORT = "port";
	public static final String PASSWORD = "password";
	public static final String VERSION_CODE = "version_code";


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

	void setAdmin(boolean b);

	boolean isAdmin();

	int startUpTimes();

	void setBlockDownload(boolean b);

	boolean isBlockDownload();

	String getServer();

	void setServer(String server);

	int getPort();

	void setPort(int port);

	String getPassword();

	void setPassword(String password);

	void setVersionCode(int versionCode);

	int getVersionCode();
}
