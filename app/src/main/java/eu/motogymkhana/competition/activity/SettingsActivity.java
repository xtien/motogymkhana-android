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

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.motogymkhana.competition.BuildConfig;
import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.R;
import eu.motogymkhana.competition.adapter.ChangeListener;
import eu.motogymkhana.competition.api.ResponseHandler;
import eu.motogymkhana.competition.api.response.ListRoundsResult;
import eu.motogymkhana.competition.api.response.SettingsResult;
import eu.motogymkhana.competition.api.response.UpdateSettingsResponse;
import eu.motogymkhana.competition.dao.CredentialDao;
import eu.motogymkhana.competition.fragment.BaseFragment;
import eu.motogymkhana.competition.fragment.ManageRoundsFragment;
import eu.motogymkhana.competition.log.MyLog;
import eu.motogymkhana.competition.model.Country;
import eu.motogymkhana.competition.model.Round;
import eu.motogymkhana.competition.notify.Notifier;
import eu.motogymkhana.competition.prefs.MyPreferences;
import eu.motogymkhana.competition.round.RoundManager;
import eu.motogymkhana.competition.settings.Settings;
import eu.motogymkhana.competition.settings.SettingsManager;
import toothpick.Scope;
import toothpick.Toothpick;

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
    protected RoundManager roundManager;

    @Inject
    protected CredentialDao credentialDao;

    @Inject
    protected MyPreferences prefs;

    @Inject
    protected Notifier notifier;

    final int[] seasons = {2015, 2016, 2017, 2018, 2019, 2020};

    private List<Round> rounds = new ArrayList<Round>();
    private List<Country> countries = new ArrayList<Country>();
    private ArrayAdapter roundsSpinAdapter;

    private int orgSeason;
    private Country orgCountry;
    private Round orgRound;

    @BindView(R.id.percentage_blue)
    protected EditText percentageBlue;

    @BindView(R.id.percentage_green)
    protected EditText percentageGreen;

    @BindView(R.id.rounds_season_result)
    protected EditText roundsForSeasonResult;

    @BindView(R.id.rounds_bib)
    protected EditText roundsForBib;

    @BindView(R.id.points)
    protected EditText pointsView;

    @BindView(R.id.date_spinner)
    protected Spinner roundsSpinner;

    @BindView(R.id.country_spinner)
    protected Spinner countrySpinner;

    @BindView(R.id.select_season_spinner)
    protected Spinner seasonSpinner;

    @BindView(R.id.admin_layout)
    protected LinearLayout adminLayout;

    @BindView(R.id.manage_rounds)
    protected Button manageRoundsButton;

    @BindView(R.id.version_string)
    protected TextView versionStringView;

    @BindView(R.id.save)
    protected Button saveButton;

    private int[] points;

    @BindView(R.id.progress_bar)
    protected ProgressBar progressBar;

    boolean admin = false;
    private boolean first = true;

    @Inject
    protected SettingsManager settingsManager;

    @Inject
    protected MyLog log;

    private Settings settings;
    private boolean firstCountrySelected = true;

    private ChangeListener listener = new ChangeListener() {

        @Override
        public void notifyDataChanged() {

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
//                    try {
//                        settings = settingsManager.getSettings(getSettingsResponseHandler);
//                    } catch (IOException e) {
//                        showAlert(e);
//                    } catch (SQLException e) {
//                        showAlert(e);
//                    }
                }
            });
        }
    };

    private ResponseHandler uploadSettingsResponseHandler = new ResponseHandler() {

        @Override
        public void onSuccess(Object object) {

            UpdateSettingsResponse settingsResponse = (UpdateSettingsResponse) object;

            if (settingsResponse.isOK()) {
                notifier.notifyDataChanged();
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                    }
                });
            } else {
                showAlert(settingsResponse.getStatus(), "uploading of settings failed");
            }
        }

        @Override
        public void onException(Exception e) {
            showAlert(e);
            log.e(LOGTAG, e);
        }

        @Override
        public void onError(int statusCode, String string) {
            log.e(LOGTAG, string);
            showAlert(statusCode, string);
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

            String pointsString = "";
            for (int point : points) {
                pointsString += Integer.toString(point) + ",";
            }
            pointsString = pointsString.substring(0, pointsString.length() - 1);

            settings.setPoints(pointsString);

            settings.setPercentageBlue(newPercentageBlue);
            settings.setPercentageGreen(newPercentageGreen);
            settings.setNumberOfRoundsForBib(newRoundsBib);
            settings.setNumberOfRoundsForSeasonResult(newRoundsSeasonResult);

            settings.setSeason(seasons[seasonSpinner.getSelectedItemPosition()]);
            settings.setCountry(countries.get(countrySpinner.getSelectedItemPosition()));

            settings.setPoints(pointsView.getText().toString());

            try {
                settingsManager.store(settings);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            progressBar.setVisibility(View.VISIBLE);
            settingsManager.uploadSettingsToServer(settings, uploadSettingsResponseHandler);
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

    private ResponseHandler getSettingsAfterCountryChangeResponseHandler = new ResponseHandler() {

        @Override
        public void onSuccess(Object object) {
            admin = credentialDao.isAdmin();
            setAdminLayout();
        }

        @Override
        public void onException(Exception e) {
            showAlert(e);
        }

        @Override
        public void onError(int statusCode, String string) {
            showAlert(statusCode, string);
        }
    };

    private AdapterView.OnItemSelectedListener countrySelectedListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            if (!firstCountrySelected) {
                Country country = countries.get(position);
                settings.setCountry(country);
                prefs.setCountry(country);
                settingsManager.getSettingsFromServer(getSettingsAfterCountryChangeResponseHandler);
            } else {
                firstCountrySelected = false;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private void setAdminLayout() {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (admin) {
                    adminLayout.setVisibility(View.VISIBLE);
                } else {
                    adminLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    private ResponseHandler roundsResponseHandler = new ResponseHandler() {

        @Override
        public void onSuccess(Object object) {

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    try {
                        setRounds();
                    } catch (IOException e) {
                        showAlert(e);
                    } catch (SQLException e) {
                        showAlert(e);
                    }
                }
            });
        }

        @Override
        public void onException(Exception e) {
            showAlert(e);
        }

        @Override
        public void onError(int statusCode, String string) {
            showAlert(statusCode, string);
        }
    };

    private ResponseHandler getSettingsResponseHandler = new ResponseHandler() {

        @Override
        public void onSuccess(Object object) {

            final SettingsResult result = (SettingsResult) object;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (result.getSettings() != null) {
                        roundManager.loadRoundsFromServer(roundsResponseHandler);
                    } else {
                        resetRounds();
                    }
                }
            });
        }

        @Override
        public void onException(Exception e) {
            showAlert(e);
        }

        @Override
        public void onError(int statusCode, String string) {
            showAlert(statusCode, string);
        }
    };

    private AdapterView.OnItemSelectedListener seasonSelectedListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            int season = seasons[position];
            prefs.setSeason(season);

            try {
                settings = settingsManager.getSettings(getSettingsResponseHandler);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }

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
    private Scope scope;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        scope = Toothpick.openScopes(Constants.DEFAULT_SCOPE, this);
        super.onCreate(savedInstanceState);
        Toothpick.inject(this, scope);

        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        points = getResources().getIntArray(R.array.qualification_points);

        admin = credentialDao.isAdmin();

        try {
            settings = settingsManager.getSettings(getSettingsResponseHandler);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
            versionStringView.setText((BuildConfig.FLAVOR.equals("dev") || BuildConfig.BUILD_TYPE.equals("debug") ? (BuildConfig.FLAVOR + " " + BuildConfig.BUILD_TYPE + " ") : "") + getPackageManager().getPackageInfo(getPackageName(), 0).versionName + " " +
                    getPackageManager().getPackageInfo(getPackageName(), 0).versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        roundsSpinAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item);
        roundsSpinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        setRoundsInit();
        setSettings();

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
                    settings = settingsManager.getSettings();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                if (settings != null) {
                    percentageBlue.setText(Integer.toString(settings.getPercentageBlue()));
                    percentageGreen.setText(Integer.toString(settings.getPercentageGreen()));
                    roundsForSeasonResult.setText(Integer.toString(settings.getRoundsForSeasonResult()));
                    roundsForBib.setText(Integer.toString(settings.getRoundsForBib()));
                }
                setAdminLayout();

                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void resetRounds() {
        roundsSpinAdapter.clear();
    }

    private void setRoundsInit() {

        roundsSpinAdapter.clear();

        try {
            rounds = roundManager.getRounds();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (rounds == null || rounds.size() == 0) {

            progressBar.setVisibility(View.VISIBLE);
            roundManager.loadRoundsFromServer(new ResponseHandler() {

                @Override
                public void onSuccess(Object object) {
                    ListRoundsResult result = (ListRoundsResult) object;
                    if (result.getRounds() != null) {
                        rounds = result.getRounds();
                        for (Round round : result.getRounds()) {
                            roundsSpinAdapter.add(round.getDateString());
                        }
                    }
                }

                @Override
                public void onException(Exception e) {

                }

                @Override
                public void onError(int statusCode, String string) {

                }
            });
        } else {
            for (Round round : rounds) {
                roundsSpinAdapter.add(round.getDateString());
            }

            try {

                int position = getRoundNumber();
                roundsSpinner.setSelection(position);
                prefs.setDate(rounds.get(position).getDate());

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void setRounds() throws IOException, SQLException {

        roundsSpinAdapter.clear();

        try {
            rounds = roundManager.getRounds();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (rounds != null && rounds.size() > 0) {

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
        notifier.registerRiderResultListener(listener);
    }

    @Override
    public void onPause() {
        super.onPause();
        notifier.unRegisterRiderResultListener(listener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toothpick.closeScope(this);
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

        if (orgRound != null && prefs.getDate() != orgRound.getDate()) {
            intent.putExtra(ROUND_CHANGED, true);
        }

        prefs.setCountry(settings.getCountry());
        prefs.setSeason(settings.getSeason());

        setResult(OK, intent);

        super.onBackPressed();
    }

    private boolean isNumeric(String s) {
        return !StringUtils.isEmpty(s) && StringUtils.isNumeric(s);
    }
}
