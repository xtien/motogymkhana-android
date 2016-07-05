/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.settings;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import eu.motogymkhana.competition.api.ResponseHandler;
import eu.motogymkhana.competition.model.Round;

/**
 * Created by christine on 21-2-16.
 * Manages settings.
 */
public interface SettingsManager {

    void getSettingsFromServer(ResponseHandler responseHandler) ;

    void uploadSettingsToServer(Settings settings, ResponseHandler responseHandler);

    int getRoundsCountingForSeasonResult();

    Settings getSettings() throws IOException, SQLException;

    void setRounds(List<Round> rounds) throws IOException, SQLException;

    void setSettings(Settings settings);
}
