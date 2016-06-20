/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.inject.Inject;

import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.R;
import eu.motogymkhana.competition.adapter.ChangeListener;
import eu.motogymkhana.competition.dao.CredentialDao;
import eu.motogymkhana.competition.dao.SettingsDao;
import eu.motogymkhana.competition.fragment.BaseFragment;
import eu.motogymkhana.competition.fragment.ManageRoundsFragment;
import eu.motogymkhana.competition.model.Country;
import eu.motogymkhana.competition.model.Round;
import eu.motogymkhana.competition.prefs.ChristinePreferences;
import eu.motogymkhana.competition.rider.RiderManager;
import eu.motogymkhana.competition.round.RoundManager;
import eu.motogymkhana.competition.settings.Settings;
import eu.motogymkhana.competition.settings.SettingsManager;
import roboguice.RoboGuice;

/**
 * Created by christine on 19-5-15.
 * Activity for editing settings. This includes both user settings and admin settings. User can select
 * season, country and round here,"Round" is the date of a parciular race. Admin can set number of rounds
 * that counts for season results (like "best 6 of 8 races"), the percentages for bib entitlement and the points
 * distribution for the end result. Bib entitlement has like "you need to finish within 105% of winner for a blue
 * bib point", or 115% for a green bib point. Also, admin can specify how many bib points a rider needs to actually
 * get the bib. "Points distribution" means "first gets 40 points, second gets 37 points, etc" on which the season
 * results are based.
 */
public class SettingsActivity extends BaseActivity {

    private static final String LOGTAG = SettingsActivity.class.getSimpleName();

    public static final String COUNTRY_CHANGED = "country_changed";
    public static final String SEASON_CHANGED = "season_changed";
    public static final String ROUND_CHANGED = "round_changed";

    private static final int OK = 101;

    @Inject
    private RoundManager roundManager;

    @Inject
    private CredentialDao credentialDao;

    @Inject
    private ChristinePreferences prefs;

    final int[] seasons = {2015, 2016, 2017};

    private List<Round> rounds = new ArrayList<Round>();
    private List<Country> countries = new ArrayList<Country>();
    private ArrayAdapter roundsSpinAdapter;

    private int orgSeason;
    private Country orgCountry;
    private Round orgRound;

    @Bind(R.id.percentage_blue)
    protected EditText percentageBlue;

    @Bind(R.id.percentage_green)
    protected EditText percentageGreen;

    @Bind(R.id.rounds_season_result)
    protected EditText roundsForSeasonResult;

    @Bind(R.id.rounds_bib)
    protected EditText roundsForBib;

    @Bind(R.id.points)
    protected EditText pointsView;

    @Bind(R.id.date_spinner)
    protected Spinner roundsSpinner;

    @Bind(R.id.country_spinner)
    protected Spinner countrySpinner;

    @Bind(R.id.select_season_spinner)
    protected Spinner seasonSpinner;

    @Bind(R.id.admin_layout)
    protected LinearLayout adminLayout;

    @Bind(R.id.manage_rounds)
    protected Button manageRoundsButton;

    @Bind(R.id.version_string)
    protected TextView versionStringView;

    @Bind(R.id.save)
    protected Button saveButton;

    private int[] points;

    @Bind(R.id.progress_bar)
    protected ProgressBar progressBar;

    boolean admin = false;
    private boolean first = true;

    @Inject
    private SettingsDao settingsDao;

    @Inject
    private SettingsManager settingsManager;

    @Inject
    private RiderManager riderManager;

    private Settings settings;
    private boolean firstCountrySelected = true;

    private ChangeListener listener = new ChangeListener() {

        @Override
        public void notifyDataChanged() {

            setRounds();

        }
    };

    private View.OnClickListener saveClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            int newPercentageBlue = 0;
            int newPercentageGreen = 0;
            int newRoundsBib = 0;
            int newRoundsSeasonResult = 0;

            if (isNumeric(percentageBlue.getText().toString())) {
                newPercentageBlue = Integer.parseInt(percentageBlue.getText().toString());
            }
            if (isNumeric(percentageGreen.getText().toString())) {
                newPercentageGreen = Integer.parseInt(percentageGreen.getText().toString());
            }
            if (isNumeric(roundsForBib.getText().toString())) {
                newRoundsBib = Integer.parseInt(roundsForBib.getText().toString());
            }
            if (isNumeric(roundsForSeasonResult.getText().toString())) {
                newRoundsSeasonResult = Integer.parseInt(roundsForSeasonResult.getText().toString());
            }

            if (settings == null) {
                settings = new Settings();
            }


            settings.setPercentageBlue(newPercentageBlue);
            settings.setPercentageGreen(newPercentageGreen);
            settings.setNumberOfRoundsForBib(newRoundsBib);
            settings.setNumberOfRoundsForSeasonResult(newRoundsSeasonResult);

            settings.setSeason(seasons[seasonSpinner.getSelectedItemPosition()]);
            settings.setCountry(countries.get(countrySpinner.getSelectedItemPosition()));

            settings.setPoints(pointsView.getText().toString());

            try {
                settingsDao.store(settings);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            settingsManager.uploadSettingsToServer(settings);
        }
    };

    private AdapterView.OnItemSelectedListener roundSelectedListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            if (!first) {
                if (rounds.size() > position) {
                    prefs.setDate(rounds.get(position).getDate());
                }
            } else {
                first = false;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private AdapterView.OnItemSelectedListener countrySelectedListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            if (!firstCountrySelected) {

                Country country = countries.get(position);
                settings.setCountry(country);
                Constants.country = country;
                setRounds();
            } else {
                firstCountrySelected = false;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private AdapterView.OnItemSelectedListener seasonSelectedListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            int season = seasons[position];
            Constants.season = season;
            try {
                settings = settingsManager.getSettings();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            setRounds();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private View.OnClickListener manageRoundsClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            Fragment fragment = new ManageRoundsFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment, ManageRoundsFragment.class.getSimpleName());
            transaction.addToBackStack(ManageRoundsFragment.class.getSimpleName());
            transaction.commit();
        }
    };

    InputFilter filter = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                char c = source.charAt(i);
                if (c == 59 || c == 46 || c == 44) {
                    return ",";
                } else if (!(Character.isDigit(c) || c == 44)) {
                    return "";
                }
            }
            return null;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        RoboGuice.getInjector(this).injectMembers(this);

        points = getResources().getIntArray(R.array.qualification_points);

        admin = credentialDao.isAdmin();

        try {
            settings = settingsDao.get();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        orgSeason = settings.getSeason();
        orgCountry = settings.getCountry();

        try {
            orgRound = roundManager.getCurrentRound();
            rounds.addAll(roundManager.getRounds());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        countries.addAll(Arrays.asList(Country.values()));

        try {
            versionStringView.setText(getPackageManager().getPackageInfo(getPackageName(), 0).versionName + " " +
                    getPackageManager().getPackageInfo(getPackageName(), 0).versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (Constants.testServer) {
            ((TextView) findViewById(R.id.testserver_string)).setVisibility(View.VISIBLE);
        }

        setSettings();

        roundsSpinAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item);
        roundsSpinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        setRounds();
        roundsSpinner.setAdapter(roundsSpinAdapter);

        try {
            Round round = roundManager.getRoundForDate(prefs.getDate());
            if (round != null) {
                Integer roundNumber = round.getNumber();
                roundsSpinner.setSelection(roundNumber != null ? roundNumber - 1 : 1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        roundsSpinner.setOnItemSelectedListener(roundSelectedListener);
        try {
            roundsSpinner.setSelection(getRoundNumber());
        } catch (SQLException e) {
            showAlert(e);
        }

        ArrayAdapter<CharSequence> countrySpinAdapter = new ArrayAdapter(this, android.R.layout
                .simple_spinner_item);
        countrySpinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        for (Country country : countries) {
            countrySpinAdapter.add(country.name());
        }

        countrySpinner.setOnItemSelectedListener(countrySelectedListener);

        countrySpinner.setAdapter(countrySpinAdapter);
        countrySpinner.setSelection(getCountryNumber());

        ArrayAdapter<CharSequence> seasonSpinAdapter = new ArrayAdapter(this, android.R.layout
                .simple_spinner_item);
        seasonSpinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        for (int season : seasons) {
            seasonSpinAdapter.add(Integer.toString(season));
        }

        seasonSpinner.setOnItemSelectedListener(seasonSelectedListener);

        seasonSpinner.setAdapter(seasonSpinAdapter);
        seasonSpinner.setSelection(getSeasonNumber());

        manageRoundsButton.setOnClickListener(manageRoundsClickListener);

        String pointsString = settings.getPointsString();
        if (pointsString == null) {
            pointsString = makePointString(points);
        }

        pointsView.setText(pointsString);

        pointsView.setFilters(new InputFilter[]{filter});

        saveButton.setOnClickListener(saveClickListener);
    }

    private String makePointString(int[] points) {
        String result = "";
        String separator = "";
        for (int pt : points) {
            result += separator + Integer.toString(pt);
            separator = ",";
        }
        return result;
    }

    private void setSettings() {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                try {
                    settings = settingsDao.get();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                if (settings != null) {
                    percentageBlue.setText(Integer.toString(settings.getPercentageBlue()));
                    percentageGreen.setText(Integer.toString(settings.getPercentageGreen()));
                    roundsForSeasonResult.setText(Integer.toString(settings.getRoundsForSeasonResult()));
                    roundsForBib.setText(Integer.toString(settings.getRoundsForBib()));
                }

                if (admin) {
                    adminLayout.setVisibility(View.VISIBLE);
                } else {
                    adminLayout.setVisibility(View.GONE);
                }

                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void setRounds() {

        roundsSpinAdapter.clear();

        try {
            rounds = roundManager.getRounds();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (rounds == null || rounds.size() == 0) {

            progressBar.setVisibility(View.VISIBLE);
            roundManager.loadRoundsFromServer();
        } else {
            for (Round round : rounds) {
                roundsSpinAdapter.add(round.getDateString());
            }

            try {

                int position = getRoundNumber();
                roundsSpinner.setSelection(position);
                prefs.setDate(rounds.get(position).getDate());

                setSettings();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        riderManager.registerRiderResultListener(listener);
    }

    @Override
    public void onPause() {
        super.onPause();
        riderManager.unRegisterRiderResultListener(listener);
    }

    private int getCountryNumber() {

        Country currentCountry = settings.getCountry();
        for (int i = 0; i < countries.size(); i++) {
            if (currentCountry == countries.get(i)) {
                return i;
            }
        }
        return 0;
    }

    private int getSeasonNumber() {

        int currentSeason = settings.getSeason();
        for (int i = 0; i < seasons.length; i++) {
            if (currentSeason == seasons[i]) {
                return i;
            }
        }
        return 0;
    }

    private int getRoundNumber() throws SQLException {

        Round round = roundManager.getRoundForDate(prefs.getDate());

        if (round != null) {
            for (int i = 0; i < rounds.size(); i++) {
                if (round.getNumber() == rounds.get(i).getNumber()) {
                    return i;
                }
            }
        }
        return 0;
    }

    @Override
    public void onBackPressed() {

        BaseFragment fragment = (BaseFragment) getSupportFragmentManager().findFragmentByTag(ManageRoundsFragment
                .class.getSimpleName());

        if (fragment != null) {
            fragment.onBackPressed();
        }

        Intent intent = new Intent();

        if (settings.getCountry() != orgCountry) {
            intent.putExtra(COUNTRY_CHANGED, true);
        }
        if (settings.getSeason() != orgSeason) {
            intent.putExtra(SEASON_CHANGED, true);
        }

        if (orgRound !=null && prefs.getDate() != orgRound.getDate()) {
            intent.putExtra(ROUND_CHANGED, true);
        }

        Constants.season = settings.getSeason();
        Constants.country = settings.getCountry();
        prefs.setCountry(Constants.country);
        prefs.setSeason(Constants.season);

        setResult(OK, intent);

        super.onBackPressed();
    }

    private boolean isNumeric(String s) {
        return !StringUtils.isEmpty(s) && StringUtils.isNumeric(s);
    }
}
