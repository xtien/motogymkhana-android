/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.sql.SQLException;

import javax.inject.Inject;

import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.R;
import eu.motogymkhana.competition.api.ResponseHandler;
import eu.motogymkhana.competition.api.response.UpdateRiderResponse;
import eu.motogymkhana.competition.log.MyLog;
import eu.motogymkhana.competition.model.Bib;
import eu.motogymkhana.competition.model.Country;
import eu.motogymkhana.competition.model.Gender;
import eu.motogymkhana.competition.model.Registration;
import eu.motogymkhana.competition.model.Rider;
import eu.motogymkhana.competition.model.Times;
import eu.motogymkhana.competition.notify.Notifier;
import eu.motogymkhana.competition.prefs.MyPreferences;
import eu.motogymkhana.competition.rider.RiderManager;
import toothpick.Scope;
import toothpick.Toothpick;

/**
 * created by Christine
 * This activity allows for entering a new rider or updating data of an existing rider. If data have changed,
 * the new rider data is uploaded to the server.
 */
public class RiderNewUpdateActivity extends BaseActivity {

    public static final String RIDER_ID = "rider_number";
    public static final String FOCUS = "focus";
    private static final String LOGTAG = RiderNewUpdateActivity.class.getSimpleName();
    private TextView errorText;

    @Inject
    protected RiderManager riderManager;

    @Inject
    protected MyPreferences prefs;

    @Inject
    protected Notifier notifier;

    @Inject
    protected MyLog log;

    Rider rider = null;
    private ResponseHandler updateRiderResponseHandler = new ResponseHandler() {

        @Override
        public void onSuccess(Object object) {

            UpdateRiderResponse response = (UpdateRiderResponse) object;
            if (response.getRider() != null) {
                try {
                    riderManager.store(response.getRider());
                } catch (SQLException e) {
                    showAlert(e);
                }
            }

            if (response.isOK()) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        finish();
                        notifier.notifyDataChanged();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        }

        @Override
        public void onException(Exception e) {
            log.e(LOGTAG, e);
        }

        @Override
        public void onError(int statusCode, String error) {
            errorText.setText(error);
        }
    };

    private ResponseHandler deleteRiderResponseHandler = new ResponseHandler() {

        @Override
        public void onSuccess(Object object) {

            UpdateRiderResponse response = (UpdateRiderResponse) object;

            if (response.isOK()) {
                finish();
                notifier.notifyDataChanged();
                progressBar.setVisibility(View.GONE);
            }
        }

        @Override
        public void onException(Exception e) {
            showAlert(e);
        }

        @Override
        public void onError(int statusCode, String string) {
            errorText.setText("Delete failed....");
        }
    };
    private Scope scope;
    private View progressBar;
    private Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        scope = Toothpick.openScopes(Constants.DEFAULT_SCOPE, this);
        super.onCreate(savedInstanceState);
        Toothpick.inject(this, scope);

        setContentView(R.layout.activity_new_rider_input);

        HandlerThread handlerThread = new HandlerThread("update");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());

        final String riderId = getIntent().getStringExtra(RIDER_ID);

        progressBar = findViewById(R.id.progress_bar);

        final EditText firstNameView = (EditText) findViewById(R.id.first_name);
        final EditText emailView = (EditText) findViewById(R.id.email);
        final EditText lastNameView = (EditText) findViewById(R.id.last_name);
        final Spinner nationalitySpinner = (Spinner) findViewById(R.id.country);
        final Spinner bibSpinner = (Spinner) findViewById(R.id.bib);
        final CheckBox genderButton = (CheckBox) findViewById(R.id.gender);
        errorText = (TextView) findViewById(R.id.error_text);
        final EditText startNumberView = (EditText) findViewById(R.id.start_number);
        final EditText bikeView = (EditText) findViewById(R.id.bike);
        final EditText riderTextView = (EditText) findViewById(R.id.rider_text);
        final CheckBox hideBox = (CheckBox) findViewById(R.id.hide);

        final ImageView riderImage = (ImageView) findViewById(R.id.rider_image);
        final ImageView bikeImage = (ImageView) findViewById(R.id.bike_image);

        Button b2016 = (Button) findViewById(R.id.up_2016);
        b2016.setVisibility(View.GONE);

        Button buttonEU = (Button) findViewById(R.id.button_eu);
        buttonEU.setVisibility(View.GONE);

        ArrayAdapter<CharSequence> countrySpinAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item);
        countrySpinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        for (Country item : Country.values()) {
            countrySpinAdapter.add(item.name());
        }

        nationalitySpinner.setAdapter(countrySpinAdapter);

        countrySpinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        BibSpinAdapter bibSpinAdapter = new BibSpinAdapter(this);
        bibSpinner.setAdapter(bibSpinAdapter);

        if (riderId != null) {

            try {
                rider = riderManager.getRiderByServerId(riderId);

                if (rider == null) {
                    showAlert(404, "rider not found");
                    finish();

                } else {

                    firstNameView.setText(rider.getFirstName());
                    lastNameView.setText(rider.getLastName());
                    genderButton.setChecked(rider.getGender() == Gender.F);
                    startNumberView.setText(Integer.toString(rider.getStartNumber()));
                    emailView.setText(rider.getEmail());
                    hideBox.setChecked(rider.isHideLastName());

                    Times times = rider.getEUTimes(prefs.getDate());
                    if (times != null) {
                        startNumberView.setText(Integer.toString(times.getStartNumber()));
                    }

                    bikeView.setText(rider.getBike());
                    riderTextView.setText(rider.getText());

                    if (rider.hasImageUrl()) {
                        Picasso.with(this).load(rider.getImageUrl()).into(riderImage);
                        riderImage.setVisibility(View.VISIBLE);
                    }
                    if (rider.hasBikeImageUrl()) {
                        Picasso.with(this).load(rider.getBikeImageUrl()).into(bikeImage);
                        bikeImage.setVisibility(View.VISIBLE);
                    }

                    if (rider.getCountry() == null) {
                        rider.setCountry(Constants.country);
                    }
                    for (int i = 0; i < Country.values().length; i++) {
                        if (Country.values()[i] == rider.getNationality()) {
                            nationalitySpinner.setSelection(i);
                            break;
                        }
                    }


                    if (rider.getBib() == null) {
                        rider.setBib(Bib.Y);
                    } else {
                        for (int i = 0; i < Bib.values().length; i++) {
                            if (Bib.values()[i] == rider.getBib()) {
                                bibSpinner.setSelection(i);
                                break;
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                errorText.setText(e.getMessage());
            }

        } else {
            rider = new Rider();
            Registration registration = new Registration(Constants.country, Constants.season, riderManager
                    .newRiderNumber());
            rider.addRegistration(registration);
        }

        nationalitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rider.setNationality(Country.values()[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bibSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rider.setBib(Bib.values()[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ((Button) findViewById(R.id.deleteButton)).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                riderManager.deleteRider(rider, deleteRiderResponseHandler);
            }
        });


        ((Button) findViewById(R.id.okButton)).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                String email = emailView.getText().toString();
                String firstName = firstNameView.getText().toString();
                String lastName = lastNameView.getText().toString();
                Gender gender = genderButton.isChecked() ? Gender.F : Gender.M;
                String nationalityString = (String) nationalitySpinner.getSelectedItem();
                Bib bib = (Bib) bibSpinner.getSelectedItem();
                String bike = bikeView.getText().toString();
                String riderText = riderTextView.getText().toString();
                int startNumber = 0;
                if (NumberUtils.isCreatable(startNumberView.getText().toString())) {
                    startNumber = Integer.parseInt(startNumberView.getText().toString());
                }
                Country nationality = Constants.country;
                if (nationalityString != null) {
                    nationality = Country.valueOf(nationalityString);
                }

                rider.setFirstName(firstName);
                rider.setLastName(lastName);
                rider.setGender(gender);
                rider.setNationality(nationality);
                rider.setCountry(Constants.country);
                rider.setSeason(Constants.season);
                rider.setBike(bike);
                rider.setText(riderText);
                rider.setBib(bib);
                rider.setEmail(email);
                rider.setHideLastName(hideBox.isChecked());

                if (startNumber != 0) {
                    rider.setStartNumber(prefs.getDate(), startNumber);
                }

                if (firstName != null && lastName != null) {
                    progressBar.setVisibility(View.VISIBLE);
                    handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            riderManager.update(rider, updateRiderResponseHandler);
                        }
                    }, 10l);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toothpick.closeScope(this);
    }

}
