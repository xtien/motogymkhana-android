package eu.motogymkhana.competition.api;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import eu.motogymkhana.competition.api.impl.RidersCallback;
import eu.motogymkhana.competition.model.Rider;
import eu.motogymkhana.competition.model.Round;

public interface ApiManager {

	ListRidersResult getRiders() throws SQLException, JsonGenerationException, JsonMappingException,
			IOException;

	void getRiders(RidersCallback callback) throws SQLException, JsonGenerationException,
			JsonMappingException, IOException;

	void updateRider(Rider rider) throws IOException;

	void uploadRiders(List<Rider> riders) throws IOException;

	void delete(Rider rider) throws IOException;

	void sendText(String text) throws IOException;

	void updateRiders(List<Rider> riders) throws IOException;

	void uploadRounds(Collection<Round> rounds) throws IOException;

	ListRoundsResult getRounds() throws IOException;

	boolean checkPassword(String customerCode, String password) throws IOException;
}
