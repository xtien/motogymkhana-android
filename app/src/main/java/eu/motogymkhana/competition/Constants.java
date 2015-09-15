package eu.motogymkhana.competition;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Constants {

    public static final boolean testServer = true;

    public static final long refreshRate = 15000l;

    public static boolean test = false;
    public static final int DATABASE_VERSION = 7;
    public static String DATABASE_NAME = "gymkhana.db";

    public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public static boolean USE_HTTPS = true;

    public static final String USER_AGENT = "motogymkhana_android";
    public static final String urlHttp = "http://";
    public static final String urlHttps = "https://";
    public static final String hostName = testServer ? "pengo.xs4all.nl" : "www.christine.nl";
    public static final int HTTPS_PORT = 9005;
    public static final int HTTP_PORT = 8085;
    public static final String CUSTOMER_CODE = "NL";
    public static final String BASE_URL_STRING = "/motogymkhana/" + CUSTOMER_CODE;

    public static final String GET_RIDERS_URL_STRING = BASE_URL_STRING + "/getRiders/";
    public static final String GET_ROUNDS_URL_STRING = BASE_URL_STRING + "/getRounds/";
    public static final String GET_UPDATE_ROUND_URL_STRING = BASE_URL_STRING + "/updateRound/";
    public static final String UPDATE_RIDER_URL_STRING = BASE_URL_STRING + "/updateRider/";
    public static final String UPDATE_RIDERS_URL_STRING = BASE_URL_STRING + "/updateRiders/";
    public static final String UPLOAD_ROUNDS_URL_STRING = BASE_URL_STRING + "/uploadRounds/";
    public static final String DELETE_RIDER_URL_STRING = BASE_URL_STRING + "/deleteRider/";
    public static final String LOAD_RIDERS_URL_STRING = BASE_URL_STRING + "/uploadRiders/";
    public static final String SEND_TEXT_URL_STRING = BASE_URL_STRING + "/updateText/";
    public static final String CHECK_PASSWORD = BASE_URL_STRING + "/pw/";

}
