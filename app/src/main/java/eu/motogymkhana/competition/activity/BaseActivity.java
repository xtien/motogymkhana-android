/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import javax.inject.Inject;

import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.dialog.MyAlert;
import eu.motogymkhana.competition.log.MyLog;
import eu.motogymkhana.competition.prefs.MyPreferences;
import eu.motogymkhana.competition.prefs.PrefsProvider;
import toothpick.Scope;
import toothpick.Toothpick;

/**
 * Created by christine on 7-2-16.
 * Contains common methods for Activities, currently only the methods for showing a dialog.
 */
public class BaseActivity extends FragmentActivity {

    private static final String LOGTAG = BaseActivity.class.getSimpleName();

    @Inject
    protected MyLog log;

    @Inject
    protected MyPreferences prefs;
    private Scope scope;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        scope = Toothpick.openScopes(Constants.DEFAULT_SCOPE, this);
        super.onCreate(savedInstanceState);
        Toothpick.inject(this, scope);
        getActionBar().setTitle("");

        PrefsProvider.setPrefs(prefs);
    }

    protected void showAlert(final Exception e) {
        log.e(LOGTAG, e);
        showAlert(e.getClass().getSimpleName() + " " + (e.getMessage() != null ? e.getMessage() : ""));
    }

    protected void showAlert(int statusCode, String message) {
        showAlert(statusCode + " " + message);
    }

    protected void showAlert(final String text) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                MyAlert alert = new MyAlert(BaseActivity.this);
                alert.setText(text);
                alert.show();
            }
        });
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Toothpick.closeScope(this);
    }
}
