package eu.motogymkhana.competition.api;

import eu.motogymkhana.competition.api.response.GymkhanaResult;
import eu.motogymkhana.competition.settings.Settings;

/**
 * Created by christine on 21-2-16.
 */
public class SettingsResult extends GymkhanaResult {

    private Settings settings;

    public Settings getSettings() {
        return settings;
    }
}
