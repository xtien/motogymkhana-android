package eu.motogymkhana.competition.api.impl;

import android.content.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.api.ApiAsync;
import eu.motogymkhana.competition.api.ApiManager;
import eu.motogymkhana.competition.api.ApiUrlHelper;
import eu.motogymkhana.competition.api.ResponseHandler;
import eu.motogymkhana.competition.api.http.MyHttp;
import eu.motogymkhana.competition.api.request.GymkhanaRequest;
import eu.motogymkhana.competition.api.response.GymkhanaResult;
import eu.motogymkhana.competition.api.response.ListRidersResult;
import eu.motogymkhana.competition.api.response.ListRoundsResult;
import eu.motogymkhana.competition.api.response.SettingsResult;
import eu.motogymkhana.competition.api.request.UpdateRiderRequest;
import eu.motogymkhana.competition.api.request.UpdateTextRequest;
import eu.motogymkhana.competition.api.request.UpdateTimesRequest;
import eu.motogymkhana.competition.api.request.UploadRidersRequest;
import eu.motogymkhana.competition.api.response.UpdateRiderResponse;
import eu.motogymkhana.competition.api.response.UpdateSettingsResponse;
import eu.motogymkhana.competition.api.response.UploadRidersResponse;
import eu.motogymkhana.competition.api.request.UploadRoundsRequest;
import eu.motogymkhana.competition.api.request.UploadSettingsRequest;
import eu.motogymkhana.competition.api.response.UploadRoundsResponse;
import eu.motogymkhana.competition.dao.CredentialDao;
import eu.motogymkhana.competition.log.MyLog;
import eu.motogymkhana.competition.model.Credential;
import eu.motogymkhana.competition.model.Rider;
import eu.motogymkhana.competition.model.Round;
import eu.motogymkhana.competition.model.Times;
import eu.motogymkhana.competition.prefs.MyPreferences;
import eu.motogymkhana.competition.settings.Settings;

public class ApiManagerImpl implements ApiManager {

    private static final String TAG = ApiManagerImpl.class.getSimpleName();

    @Inject
    private ApiUrlHelper apiUrlHelper;

    @Inject
    private MyHttp http;

    @Inject
    private MyLog log;

    @Inject
    private Context context;

    @Inject
    private ObjectMapper mapper;

    @Inject
    private MyPreferences prefs;

    @Inject
    private CredentialDao credentialDao;

    @Inject
    private ApiAsync apiAsync;

    @Override
    public void updateRider(Rider rider, ResponseHandler responseHandler)  {
        UpdateRiderRequest request = new UpdateRiderRequest(rider);
        setPW(request);
        String input = null;
        try {
            input = mapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            responseHandler.onException(e);
        }
        apiAsync.post(apiUrlHelper.getUpdateRiderUrl(), input, responseHandler, UpdateRiderResponse.class);
    }

    @Override
    public void updateTimes(Times times, ResponseHandler responseHandler)  {
        UpdateTimesRequest request = new UpdateTimesRequest(times);
        setPW(request);
        String input = null;
        try {
            input = mapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            responseHandler.onException(e);
        }
        apiAsync.post(apiUrlHelper.getUpdateTimesUrl(), input, responseHandler, UpdateRiderResponse.class);
    }

    @Override
    public void getSettings(ResponseHandler responseHandler) {
        SettingsResult result = null;
        GymkhanaRequest request = new GymkhanaRequest(Constants.country, Constants.season);
        String input = null;
        try {
            input = mapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            responseHandler.onException(e);
        }
        apiAsync.post(apiUrlHelper.getSettingsUrl(), input, responseHandler, SettingsResult.class);
    }

    @Override
    public void uploadSettings(Settings settings, ResponseHandler responseHandler) {
        UploadSettingsRequest request = new UploadSettingsRequest(settings);
        setPW(request);
        String input = null;
        try {
            input = mapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            responseHandler.onException(e);
        }
        apiAsync.post(apiUrlHelper.getUploadSettingsUrl(), input, responseHandler, UpdateSettingsResponse.class);
    }

    @Override
    public void getRiders(ResponseHandler responseHandler) {
        ListRidersResult result = null;
        GymkhanaRequest request = new GymkhanaRequest(Constants.country, Constants.season);
        String input = null;
        try {
            input = mapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            responseHandler.onException(e);
        }
        apiAsync.post(apiUrlHelper.getRidersUrl(), input, responseHandler, ListRidersResult.class);
    }

    @Override
    public void uploadRiders(List<Rider> riders, ResponseHandler responseHandler)  {
        UploadRidersRequest request = new UploadRidersRequest(riders);
        setPW(request);
        String input = null;
        try {
            input = mapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            responseHandler.onException(e);
        }
        apiAsync.post(apiUrlHelper.getUploadRidersUrl(), input, responseHandler, UploadRidersResponse.class);
    }

    @Override
    public void uploadRounds(Collection<Round> rounds, ResponseHandler responseHandler)  {
        UploadRoundsRequest request = new UploadRoundsRequest(rounds);
        setPW(request);
        String input = null;
        try {
            input = mapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            responseHandler.onException(e);
        }
        apiAsync.post(apiUrlHelper.getUploadRoundsUrl(), input, responseHandler, UploadRoundsResponse.class);
    }

    @Override
    public void getRounds(ResponseHandler responseHandler) {
        GymkhanaRequest request = new GymkhanaRequest(Constants.country, Constants.season);
        String input = null;
        try {
            input = mapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            responseHandler.onException(e);
        }
        apiAsync.post(apiUrlHelper.getRoundsUrl(), input, responseHandler, ListRoundsResult.class);
    }

    @Override
    public void checkPassword(String password, ResponseHandler responseHandler) {

        GymkhanaRequest request = new GymkhanaRequest(Constants.country, Constants.season);
        request.setPassword(password);
        String input = null;
        try {
            input = mapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            responseHandler.onException(e);
        }
        apiAsync.post(apiUrlHelper.getCheckPasswordUrl(), input, responseHandler, GymkhanaResult.class);
    }

    @Override
    public void delete(Rider rider, ResponseHandler responseHandler)  {

        UpdateRiderRequest request = new UpdateRiderRequest(rider);
        setPW(request);
        String input = null;
        try {
            input = mapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            responseHandler.onException(e);
        }
        apiAsync.post(apiUrlHelper.getDeleteRiderUrl(), input, responseHandler, UpdateRiderResponse.class);
    }

    @Override
    public void sendText(String text, ResponseHandler responseHandler)  {

        UpdateTextRequest request = new UpdateTextRequest(text);
        setPW(request);
        String input = null;
        try {
            input = mapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            responseHandler.onException(e);
        }
        apiAsync.post(apiUrlHelper.getSendTextUrl(), input, responseHandler, GymkhanaResult.class);
    }

    private void setPW(GymkhanaRequest request) {

        try {
            Credential credential = credentialDao.get();
            if (credential != null) {
                request.setPassword(credential.getPassword());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        request.setCountry(Constants.country);
    }
}
