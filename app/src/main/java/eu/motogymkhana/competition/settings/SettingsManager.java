package eu.motogymkhana.competition.settings;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by christine on 21-2-16.
 */
public interface SettingsManager {

    void getSettingsFromServerAsync() throws IOException, SQLException;

    public Settings getSettingsFromServer() throws IOException, SQLException;

    public void uploadSettingsToServer(Settings settings);

    int getRoundsCountingForSeasonResult();

    int getRoundsForBib();

    Settings getSettings() throws IOException, SQLException;
}
