/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.module;

import android.content.Context;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.motogymkhana.competition.api.ApiAsync;
import eu.motogymkhana.competition.api.ApiManager;
import eu.motogymkhana.competition.api.ApiUrlHelper;
import eu.motogymkhana.competition.api.http.impl.ObjectMapperProvider;
import eu.motogymkhana.competition.api.impl.ApiAsyncImpl;
import eu.motogymkhana.competition.api.impl.ApiManagerImpl;
import eu.motogymkhana.competition.api.impl.ApiUrlHelperImpl;
import eu.motogymkhana.competition.dao.CredentialDao;
import eu.motogymkhana.competition.dao.RegistrationDao;
import eu.motogymkhana.competition.dao.RiderDao;
import eu.motogymkhana.competition.dao.RoundDao;
import eu.motogymkhana.competition.dao.SettingsDao;
import eu.motogymkhana.competition.dao.TimesDao;
import eu.motogymkhana.competition.dao.provider.CredentialDaoProvider;
import eu.motogymkhana.competition.dao.provider.RegistrationDaoProvider;
import eu.motogymkhana.competition.dao.provider.RiderDaoProvider;
import eu.motogymkhana.competition.dao.provider.RoundDaoProvider;
import eu.motogymkhana.competition.dao.provider.SettingsDaoProvider;
import eu.motogymkhana.competition.dao.provider.TimesDaoProvider;
import eu.motogymkhana.competition.db.GymkhanaDatabaseHelper;
import eu.motogymkhana.competition.db.GymkhanaDatabaseHelperProvider;
import eu.motogymkhana.competition.log.MyLog;
import eu.motogymkhana.competition.log.impl.MyLogImpl;
import eu.motogymkhana.competition.notify.Notifier;
import eu.motogymkhana.competition.notify.impl.NotifierImpl;
import eu.motogymkhana.competition.prefs.MyPreferences;
import eu.motogymkhana.competition.prefs.impl.MyPreferencesImpl;
import eu.motogymkhana.competition.rider.RiderManager;
import eu.motogymkhana.competition.rider.impl.RiderManagerImpl;
import eu.motogymkhana.competition.round.RoundManager;
import eu.motogymkhana.competition.round.impl.RoundManagerImpl;
import eu.motogymkhana.competition.settings.SettingsManager;
import eu.motogymkhana.competition.settings.impl.SettingsManagerImpl;
import toothpick.config.Module;

/**
 * Created by Christine
 * This module file contains  bindings for the app that are the same in test and release.
 */
public class GymkhanaModule extends Module {

    public GymkhanaModule(final Context context) {

        bind(Context.class).toInstance(context);

        bind(MyLog.class).to(MyLogImpl.class);
        bind(Notifier.class).to(NotifierImpl.class);
        bind(ObjectMapper.class).toProvider(ObjectMapperProvider.class);

        bind(RiderManager.class).to(RiderManagerImpl.class);
        bind(RoundManager.class).to(RoundManagerImpl.class);
        bind(SettingsManager.class).to(SettingsManagerImpl.class);
        bind(MyPreferences.class).to(MyPreferencesImpl.class);

        bind(GymkhanaDatabaseHelper.class).toProvider(GymkhanaDatabaseHelperProvider.class);

        bind(ApiManager.class).to(ApiManagerImpl.class);
        bind(ApiUrlHelper.class).to(ApiUrlHelperImpl.class);

        // db
        bind(RiderDao.class).toProvider(RiderDaoProvider.class);
        bind(RoundDao.class).toProvider(RoundDaoProvider.class);
        bind(TimesDao.class).toProvider(TimesDaoProvider.class);
        bind(RegistrationDao.class).toProvider(RegistrationDaoProvider.class);
        bind(CredentialDao.class).toProvider(CredentialDaoProvider.class);
        bind(SettingsDao.class).toProvider(SettingsDaoProvider.class);
        bind(ApiAsync.class).to(ApiAsyncImpl.class);
    }
}
