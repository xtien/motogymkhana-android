/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.sql.SQLException;

import javax.inject.Inject;

import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.R;
import eu.motogymkhana.competition.model.Bib;
import eu.motogymkhana.competition.model.Registration;
import eu.motogymkhana.competition.model.Rider;
import eu.motogymkhana.competition.rider.RiderManager;
import toothpick.Scope;
import toothpick.Toothpick;

/**
 * created by Christine
 * This activity shows rider details.
 */
public class RiderViewActivity extends BaseActivity {

    public static final String RIDER_ID = "rider_id";
    public static final String FOCUS = "focus";

    @Inject
    protected RiderManager riderManager;
    private int number = 99;

    Rider rider = null;
    private Scope scope;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        scope = Toothpick.openScopes(Constants.DEFAULT_SCOPE, this);
        super.onCreate(savedInstanceState);
        Toothpick.inject(this, scope);

        setContentView(R.layout.activity_view_rider);

        final TextView firstNameView = (TextView) findViewById(R.id.first_name);
        final TextView lastNameView = (TextView) findViewById(R.id.last_name);
        final TextView numberView = (TextView) findViewById(R.id.number);
        final TextView countryView = (TextView) findViewById(R.id.country);
        final TextView bibView = (TextView) findViewById(R.id.bib);
        final TextView genderView = (TextView) findViewById(R.id.gender);
        numberView.setText(Integer.toString(number));
        final TextView errorText = (TextView) findViewById(R.id.error_text);
        final String riderId = getIntent().getStringExtra(RIDER_ID);
        final TextView bikeView = (TextView) findViewById(R.id.bike);
        final TextView riderTextView = (TextView) findViewById(R.id.rider_text);

        final ImageView riderImage = (ImageView) findViewById(R.id.rider_image);
        final ImageView bikeImage = (ImageView) findViewById(R.id.bike_image);

        if (riderId != null) {

            try {
                rider = riderManager.getRiderByServerId(riderId);

                firstNameView.setText(rider.getFirstName());
                lastNameView.setText(rider.getLastName());
                numberView.setText(rider.getRiderNumberString());
                genderView.setText(rider.getGender().name());
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

                countryView.setText(rider.getNationality().name());
                Registration registration = rider.getRegistration();
                if (registration != null) {
                    bibView.setText(registration.getBib().name());
                } else {
                    bibView.setText(Bib.Y.name());
                }

            } catch (SQLException e) {
                e.printStackTrace();
                errorText.setText(e.getMessage());
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toothpick.closeScope(this);
    }
}
