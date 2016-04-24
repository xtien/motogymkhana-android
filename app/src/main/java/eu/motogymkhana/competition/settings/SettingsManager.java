package eu.motogymkhana.competition.settings;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import eu.motogymkhana.competition.model.Round;

/**
 * Created by christine on 21-2-16.
 */
public interface SettingsManager {

    void getSettingsFromServerAsync() ;

     Settings getSettingsFromServer() throws IOException, SQLException;

     void uploadSettingsToServer(Settings settings);

    int getRoundsCountingForSeasonResult();

    int getRoundsForBib();

    Settings getSettings() throws IOException, SQLException;

    void setRounds(List<Round> rounds) throws IOException, SQLException;
}
