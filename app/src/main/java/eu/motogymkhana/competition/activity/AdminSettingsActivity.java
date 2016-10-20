/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.inject.Inject;

import eu.motogymkhana.competition.R;
import eu.motogymkhana.competition.prefs.MyPreferences;
import roboguice.RoboGuice;

/**
 * Created by christine on 19-5-15.
 * Allows admin to set a different server for the app.
 */
public class AdminSettingsActivity extends BaseActivity {

    @Inject
    private MyPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin_settings);
        RoboGuice.getInjector(this).injectMembers(this);

        final EditText serverView = (EditText) findViewById(R.id.server);
        final EditText portView = (EditText) findViewById(R.id.port);
        final TextView errorView = (TextView) findViewById(R.id.error_text);

        serverView.setText(prefs.getServer());
        portView.setText(Integer.toString(prefs.getPort()));

        Button okButton = (Button) findViewById(R.id.okButton);

        okButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                prefs.setServer(serverView.getText().toString());
                prefs.setPort(Integer.parseInt(portView.getText().toString()));
                finish();
            }
        });
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        RoboGuice.destroyInjector(this);
    }

}
