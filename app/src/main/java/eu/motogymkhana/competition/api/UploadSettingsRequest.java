package eu.motogymkhana.competition.api;

import eu.motogymkhana.competition.settings.Settings;

/**
 * Created by christine on 14-2-16.
 */
public class UploadSettingsRequest extends GymkhanaRequest {

    private Settings settings;

    public UploadSettingsRequest() {
    }

    public UploadSettingsRequest(Settings settings) {
        this.settings = settings;
    }

    public Settings getSettings() {
        return settings;
    }
}
