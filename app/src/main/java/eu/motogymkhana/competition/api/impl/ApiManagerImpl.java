package eu.motogymkhana.competition.api.impl;

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.api.ApiManager;
import eu.motogymkhana.competition.api.ApiUrlHelper;
import eu.motogymkhana.competition.api.GetRidersFromServerTask;
import eu.motogymkhana.competition.api.GymkhanaRequest;
import eu.motogymkhana.competition.api.GymkhanaResult;
import eu.motogymkhana.competition.api.ListRidersResult;
import eu.motogymkhana.competition.api.ListRoundsResult;
import eu.motogymkhana.competition.api.SettingsResult;
import eu.motogymkhana.competition.api.UpdateRiderRequest;
import eu.motogymkhana.competition.api.UpdateTextRequest;
import eu.motogymkhana.competition.api.UpdateTimesRequest;
import eu.motogymkhana.competition.api.UploadRidersRequest;
import eu.motogymkhana.competition.api.UploadRidersResponse;
import eu.motogymkhana.competition.api.UploadRoundsRequest;
import eu.motogymkhana.competition.api.UploadSettingsRequest;
import eu.motogymkhana.competition.api.http.HttpResultWrapper;
import eu.motogymkhana.competition.api.http.MyHttp;
import eu.motogymkhana.competition.dao.CredentialDao;
import eu.motogymkhana.competition.model.Credential;
import eu.motogymkhana.competition.model.Rider;
import eu.motogymkhana.competition.model.Round;
import eu.motogymkhana.competition.model.Times;
import eu.motogymkhana.competition.prefs.ChristinePreferences;
import eu.motogymkhana.competition.settings.Settings;

public class ApiManagerImpl implements ApiManager {

    private static final String TAG = ApiManagerImpl.class.getSimpleName();

    @Inject
    private ApiUrlHelper apiUrlHelper;

    @Inject
    private MyHttp http;

    @Inject
    private Context context;

    @Inject
    private ObjectMapper mapper;

    @Inject
    private ChristinePreferences prefs;

    @Inject
    private CredentialDao credentialDao;

    @Override
    public void getRiders(RidersCallback callback) throws SQLException, JsonGenerationException,
            JsonMappingException, IOException {

        new GetRidersFromServerTask(context, callback).execute();
    }

    @Override
    public void updateRider(Rider rider) throws IOException {

        UpdateRiderRequest request = new UpdateRiderRequest(rider);
        setPW(request);
        String json = mapper.writeValueAsString(request);

        http.postStringFromUrl(apiUrlHelper.getUpdateRiderUrl(), json);
    }

    @Override
    public void updateTimes(Times times) throws IOException {

        UpdateTimesRequest request = new UpdateTimesRequest(times);
        setPW(request);
        String json = mapper.writeValueAsString(request);

        http.postStringFromUrl(apiUrlHelper.getUpdateTimesUrl(), json);
    }

    @Override
    public SettingsResult getSettings() throws IOException {

        SettingsResult result = null;
        GymkhanaRequest request = new GymkhanaRequest(Constants.country, Constants.season);
        String input = mapper.writeValueAsString(request);

        HttpResultWrapper httpResult = http.postStringFromUrl(apiUrlHelper.getSettingsUrl(), input);

        if (httpResult != null && httpResult.getStatusCode() == 200) {
            result = mapper.readValue(httpResult.getString(), SettingsResult.class);
        } else {
            Log.e(TAG, "Settings not found ");
        }

        return result;
    }

    @Override
    public void uploadSettings(Settings settings) throws IOException {

        UploadSettingsRequest request = new UploadSettingsRequest(settings);
        setPW(request);

        String json = mapper.writeValueAsString(request);

        http.postStringFromUrl(apiUrlHelper.getUploadSettingsUrl(), json);
    }

    @Override
    public ListRidersResult getRiders() throws SQLException, JsonGenerationException, JsonMappingException,
            IOException {

        ListRidersResult result = null;
        GymkhanaRequest request = new GymkhanaRequest(Constants.country, Constants.season);
        String input = mapper.writeValueAsString(request);

        HttpResultWrapper httpResult = http.postStringFromUrl(apiUrlHelper.getRidersUrl(), input);

        if (httpResult != null && httpResult.getStatusCode() == 200) {
            result = mapper.readValue(httpResult.getString(), ListRidersResult.class);
        } else {
            Log.e(TAG, "Riders not found ");
        }

        return result;
    }

    @Override
    public void uploadRiders(List<Rider> riders) throws IOException {

        UploadRidersRequest request = new UploadRidersRequest(riders);
        setPW(request);

        String json = mapper.writeValueAsString(request);

        http.postStringFromUrl(apiUrlHelper.getUploadRidersUrl(), json);
    }

    @Override
    public void updateRiders(List<Rider> riders) throws IOException {

        UploadRidersRequest request = new UploadRidersRequest(riders);
        setPW(request);

        String json = mapper.writeValueAsString(request);

        HttpResultWrapper result = http.postStringFromUrl(apiUrlHelper.getUpdateRidersUrl(), json);

        if (result != null && result.getStatusCode() == 200) {
            UploadRidersResponse response = mapper.readValue(result.getString(), UploadRidersResponse.class);
        }
    }

    @Override
    public void uploadRounds(Collection<Round> rounds) throws IOException {

        UploadRoundsRequest request = new UploadRoundsRequest(rounds);
        setPW(request);

        String json = mapper.writeValueAsString(request);

        HttpResultWrapper result = http.postStringFromUrl(apiUrlHelper.getUploadRoundsUrl(), json);

        if (result != null && result.getStatusCode() == 200) {
            UploadRidersResponse response = mapper.readValue(result.getString(), UploadRidersResponse.class);
        }
    }

    @Override
    public ListRoundsResult getRounds() throws IOException {

        ListRoundsResult result = null;

        GymkhanaRequest request = new GymkhanaRequest(Constants.country, Constants.season);
        String input = mapper.writeValueAsString(request);

        HttpResultWrapper httpResult = http.postStringFromUrl(apiUrlHelper.getRoundsUrl(), input);

        if (httpResult != null && httpResult.getStatusCode() == 200) {
            result = mapper.readValue(httpResult.getString(), ListRoundsResult.class);
        } else {
            Log.e(TAG, "Rounds not found: ");
        }

        return result;
    }

    @Override
    public boolean checkPassword(String password) throws IOException {

        GymkhanaRequest request = new GymkhanaRequest(Constants.country, Constants.season);
        request.setPassword(password);
        String json = mapper.writeValueAsString(request);

        HttpResultWrapper result = http.postStringFromUrl(apiUrlHelper.getCheckPasswordUrl(), json);

        if (result != null && result.getStatusCode() == 200) {
            GymkhanaResult passwordResult = mapper.readValue(result.getString(), GymkhanaResult.class);
            return passwordResult != null && passwordResult.getResultCode() == 0;
        } else {
            return false;
        }
    }

    @Override
    public void delete(Rider rider) throws IOException {

        UpdateRiderRequest request = new UpdateRiderRequest(rider);
        setPW(request);
        String json = mapper.writeValueAsString(request);

        http.postStringFromUrl(apiUrlHelper.getDeleteRiderUrl(), json);
    }

    @Override
    public void sendText(String text) throws IOException {

        UpdateTextRequest request = new UpdateTextRequest(text);
        setPW(request);
        String json = mapper.writeValueAsString(request);
        http.postStringFromUrl(apiUrlHelper.getSendTextUrl(), json);
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
