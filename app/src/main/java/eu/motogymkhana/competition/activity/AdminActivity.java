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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.inject.Inject;

import java.sql.SQLException;

import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.R;
import eu.motogymkhana.competition.api.ApiManager;
import eu.motogymkhana.competition.api.ResponseHandler;
import eu.motogymkhana.competition.api.response.GymkhanaResult;
import eu.motogymkhana.competition.dao.CredentialDao;
import eu.motogymkhana.competition.model.Credential;
import eu.motogymkhana.competition.prefs.MyPreferences;
import roboguice.RoboGuice;

/**
 * Created by christine on 19-5-15.
 * Activity for entering password as an admin.
 */
public class AdminActivity extends BaseActivity {

    private String noPw;
    private String pwShort;

    @Inject
    private MyPreferences prefs;

    @Inject
    private CredentialDao credentialDao;

    @Inject
    private ApiManager api;

    private Credential credential;
    private ProgressBar progressBar;
    private TextView errorView;
    private EditText pwView;

    private ResponseHandler checkPasswordResponseHandler = new ResponseHandler() {

        @Override
        public void onSuccess(Object object) {

            final GymkhanaResult result = (GymkhanaResult) object;

            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    try {
                        progressBar.setVisibility(View.GONE);

                        if (result.isOK()) {
                            credential.setAdmin(true);
                            credential.setPassword(pwView.getText().toString());
                            credentialDao.store(credential);
                            finish();
                        } else {
                            errorView.setText(noPw);
                        }
                    } catch (Exception e) {
                        credential.setAdmin(false);
                        credential.setPassword(pwView.getText().toString());
                        try {
                            credentialDao.store(credential);
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                            showAlert(e);
                        }
                    }
                }
            });
        }

        @Override
        public void onException(Exception e) {

        }

        @Override
        public void onError(int statusCode, String string) {

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin);
        RoboGuice.getInjector(this).injectMembers(this);

        noPw = getString(R.string.no_password);
        pwShort = getString(R.string.password_too_short);


        pwView = (EditText) findViewById(R.id.password);
        errorView = (TextView) findViewById(R.id.error_text);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        try {
            credential = credentialDao.get();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (credential == null) {
            credential = new Credential();
            credential.setCountry(Constants.country);
        }

        Button okButton = (Button) findViewById(R.id.okButton);

        okButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (pwView.getText().toString().length() < 6) {
                    errorView.setText(pwShort);

                } else {

                    progressBar.setVisibility(View.VISIBLE);
                    api.checkPassword(pwView.getText().toString(), checkPasswordResponseHandler);
                }
            }
        });
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        RoboGuice.destroyInjector(this);
    }

}
