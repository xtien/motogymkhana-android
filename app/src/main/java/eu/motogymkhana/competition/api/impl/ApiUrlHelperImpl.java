package eu.motogymkhana.competition.api.impl;

import javax.inject.Inject;

import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.Server;
import eu.motogymkhana.competition.api.ApiUrlHelper;
import eu.motogymkhana.competition.prefs.MyPreferences;

public class ApiUrlHelperImpl implements ApiUrlHelper {

	private final String httpString = Server.urlHttp;
	private final int port = Server.HTTP_PORT;
	private String hostName = Server.hostName;

	@Inject
	public ApiUrlHelperImpl(MyPreferences prefs){
		hostName = prefs.getServer();
	}

	@Override
	public String getRidersUrl() {
		return httpString + hostName + ":" + port + Constants.GET_RIDERS_URL_STRING;
	}

	@Override
	public String getRoundsUrl() {
		return httpString + hostName + ":" + port + Constants.GET_ROUNDS_URL_STRING;
	}

	@Override
	public String getUpdateRoundUrl() {
		return httpString + hostName + ":" + port + Constants.GET_UPDATE_ROUND_URL_STRING;
	}

	@Override
	public String getUpdateRiderUrl() {
		return httpString + hostName + ":" + port + Constants.UPDATE_RIDER_URL_STRING;
	}

	@Override
	public String getUpdateTimesUrl() {
		return httpString + hostName + ":" + port + Constants.UPDATE_TIMES_URL_STRING;
	}

	@Override
	public String getDeleteRiderUrl() {
		return httpString + hostName + ":" + port + Constants.DELETE_RIDER_URL_STRING;
	}

	@Override
	public String getUploadRidersUrl() {
		return httpString + hostName + ":" + port + Constants.LOAD_RIDERS_URL_STRING;
	}

	@Override
	public String getSendTextUrl() {
		return httpString + hostName + ":" + port + Constants.SEND_TEXT_URL_STRING;
	}

	@Override
	public String getUpdateRidersUrl() {
			return httpString + hostName + ":" + port + Constants.UPDATE_RIDERS_URL_STRING;
		}

	@Override
	public String getUploadRoundsUrl() {
		return httpString + hostName + ":" + port + Constants.UPLOAD_ROUNDS_URL_STRING;
	}

	@Override
	public String getCheckPasswordUrl() {
		return httpString + hostName + ":" + port + Constants.CHECK_PASSWORD;
	}

	@Override
	public String getSettingsUrl() {
		return httpString + hostName + ":" + port + Constants.GET_SETTINGS_URL_STRING;
	}

	@Override
	public String getUploadSettingsUrl() {
		return httpString + hostName + ":" + port + Constants.UPLOAD_SETTINGS_URL_STRING;
	}
}
