package eu.motogymkhana.competition.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.inject.Inject;

import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

/**
 * Created by christine on 19-5-15.
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

    private EditText percentageBlue;
    private EditText percentageGreen;
    private EditText roundsForSeasonResult;
    private EditText roundsForBib;

    private Spinner roundsSpinner;
    private Spinner countrySpinner;
    private Spinner seasonSpinner;
    private LinearLayout adminLayout;

    boolean admin = false;

    @Inject
    private SettingsDao settingsDao;

    @Inject
    private SettingsManager settingsManager;

    @Inject
    private RiderManager riderManager;

    private Settings settings;

    private ChangeListener listener = new ChangeListener() {

        @Override
        public void notifyDataChanged() {

            setRounds();

            try {

                roundsSpinner.setSelection(getRoundNumber());

                setSettings();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        try {
            settings = settingsDao.get();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        orgSeason = settings.getSeason();
        orgCountry = settings.getCountry();

        try {
            orgRound = roundManager.getRound();
            rounds.addAll(roundManager.getRounds());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        countries.addAll(Arrays.asList(Country.values()));

        percentageBlue = (EditText) findViewById(R.id.percentage_blue);
        percentageGreen = (EditText) findViewById(R.id.percentage_green);
        roundsForSeasonResult = (EditText) findViewById(R.id.rounds_season_result);
        roundsForBib = (EditText) findViewById(R.id.rounds_bib);
        adminLayout = (LinearLayout) findViewById(R.id.admin_layout);

        roundsSpinner = (Spinner) findViewById(R.id.date_spinner);
        countrySpinner = (Spinner) findViewById(R.id.country_spinner);
        seasonSpinner = (Spinner) findViewById(R.id.select_season_spinner);

        final Button manageRoundsButton = (Button) findViewById(R.id.manage_rounds);

        final TextView versionStringView = (TextView) findViewById(R.id.version_string);
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
            Integer roundNumber = roundManager.getRoundNumber();
            roundsSpinner.setSelection(roundNumber != null ? roundNumber - 1 : 1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        roundsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (rounds.size() > position) {
                    try {
                        roundManager.setRound(rounds.get(position));
                    } catch (SQLException e) {
                        e.printStackTrace();
                        showAlert(e);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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

        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Country country = countries.get(position);
                settings.setCountry(country);
                Constants.country = country;
                setRounds();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        countrySpinner.setAdapter(countrySpinAdapter);
        countrySpinner.setSelection(getCountryNumber());

        ArrayAdapter<CharSequence> seasonSpinAdapter = new ArrayAdapter(this, android.R.layout
                .simple_spinner_item);
        seasonSpinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        for (int season : seasons) {
            seasonSpinAdapter.add(Integer.toString(season));
        }

        seasonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

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
        });

        seasonSpinner.setAdapter(seasonSpinAdapter);
        seasonSpinner.setSelection(getSeasonNumber());

        manageRoundsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Fragment fragment = new ManageRoundsFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment, ManageRoundsFragment.class.getSimpleName());
                transaction.addToBackStack(ManageRoundsFragment.class.getSimpleName());
                transaction.commit();
            }
        });

        admin = credentialDao.isAdmin();
    }

    private void setSettings() {

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
    }

    private void setRounds() {

        roundsSpinAdapter.clear();

        try {
            for (Round round : roundManager.getRounds()) {
                roundsSpinAdapter.add(round.getDateString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(e);
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
            Log.d(LOGTAG, "currentCountry = " + currentCountry + " " + countries.get(i));
            if (currentCountry == countries.get(i)) {
                return i;
            }
        }
        return 0;
    }

    private int getSeasonNumber() {

        int currentSeason = settings.getSeason();
        for (int i = 0; i < seasons.length; i++) {
            Log.d(LOGTAG, "currentSeason = " + currentSeason + " " + seasons[i]);
            if (currentSeason == seasons[i]) {
                Log.d(LOGTAG, "return " + i);
                return i;
            }
        }
        return 0;
    }

    private int getRoundNumber() throws SQLException {

        Round currentRound = roundManager.getRound();

        if (currentRound != null) {
            for (int i = 0; i < rounds.size(); i++) {
                if (currentRound.get_id() == rounds.get(i).get_id()) {
                    Log.d(LOGTAG, "return " + i);
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

        Round currentRound = null;
        try {
            currentRound = roundManager.getRound();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (currentRound == null || orgRound == null || currentRound.getDate() != orgRound.getDate()) {
            intent.putExtra(ROUND_CHANGED, true);
        }

        if (admin) {
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

            if (newPercentageBlue != settings.getPercentageBlue() ||
                    newPercentageBlue != settings.getPercentageBlue() ||
                    newRoundsBib != settings.getRoundsForBib() ||
                    newRoundsSeasonResult != settings.getRoundsForSeasonResult()) {

                settings.setPercentageBlue(newPercentageBlue);
                settings.setPercentageGreen(newPercentageGreen);
                settings.setNumberOfRoundsForBib(newRoundsBib);
                settings.setNumberOfRoundsForSeasonResult(newRoundsSeasonResult);

                settings.setSeason(seasons[seasonSpinner.getSelectedItemPosition()]);
                settings.setCountry(countries.get(countrySpinner.getSelectedItemPosition()));

                try {
                    settingsDao.store(settings);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                settingsManager.uploadSettingsToServer(settings);
            }
        }

        Constants.season = settings.getSeason();
        Constants.country = settings.getCountry();
        prefs.setCountry(Constants.country);
        prefs.setSeason(Constants.season);

        setResult(OK, intent);

        setRounds();
        super.onBackPressed();
    }

    private boolean isNumeric(String s) {
        return !StringUtils.isEmpty(s) && StringUtils.isNumeric(s);
    }
}
