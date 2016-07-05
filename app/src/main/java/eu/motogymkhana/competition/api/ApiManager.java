package eu.motogymkhana.competition.api;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.squareup.picasso.Downloader;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import eu.motogymkhana.competition.api.impl.RidersCallback;
import eu.motogymkhana.competition.api.response.ListRidersResult;
import eu.motogymkhana.competition.api.response.ListRoundsResult;
import eu.motogymkhana.competition.api.response.SettingsResult;
import eu.motogymkhana.competition.model.Rider;
import eu.motogymkhana.competition.model.Round;
import eu.motogymkhana.competition.model.Times;
import eu.motogymkhana.competition.settings.Settings;

public interface ApiManager {

	void getRiders(ResponseHandler responseHandler);

	void updateRider(Rider rider, ResponseHandler responseHandler);

	void uploadRiders(List<Rider> riders, ResponseHandler responseHandler);

	void delete(Rider rider, ResponseHandler responseHandler) ;

	void sendText(String text, ResponseHandler responseHandler);

	void uploadRounds(Collection<Round> rounds, ResponseHandler responseHandler);

	void getRounds(ResponseHandler responseHandler);

    void checkPassword(String password,ResponseHandler responseHandler);

	void updateTimes(Times times, ResponseHandler responseHandler);

	void getSettings( ResponseHandler responseHandler);

	void uploadSettings(Settings settings, ResponseHandler responseHandler);
}
