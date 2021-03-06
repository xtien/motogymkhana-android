/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.api.response;

import eu.motogymkhana.competition.settings.Settings;

/**
 * Created by christine on 21-2-16.
 */
public class SettingsResult extends GymkhanaResult {

    private Settings settings;

    public Settings getSettings(){
        return settings;
    }
}
