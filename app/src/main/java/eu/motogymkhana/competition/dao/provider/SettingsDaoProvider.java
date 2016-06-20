/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.dao.provider;

import com.google.inject.Inject;
import com.google.inject.Provider;

import java.sql.SQLException;

import eu.motogymkhana.competition.dao.SettingsDao;
import eu.motogymkhana.competition.db.GymkhanaDatabaseHelper;
import eu.motogymkhana.competition.settings.Settings;

/**
 * created by Christine
 * utility class for injecting a dao class
 */
public class SettingsDaoProvider implements Provider<SettingsDao> {

	@Inject
	private GymkhanaDatabaseHelper helper;

	@Override
	public SettingsDao get() {

		SettingsDao dao = null;

		try {
			dao = (SettingsDao) helper.getDAO(Settings.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return dao;
	}
}
