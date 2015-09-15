package eu.motogymkhana.competition.activity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.inject.Inject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.R;
import eu.motogymkhana.competition.model.Round;
import eu.motogymkhana.competition.round.RoundManager;
import roboguice.activity.RoboActivity;

/**
 * Created by christine on 19-5-15.
 */
public class SelectDateActivity extends RoboActivity {

    @Inject
    private RoundManager roundManager;

    private List<Round> rounds = new ArrayList<Round>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_select_date);

        try {
            rounds.addAll(roundManager.getRounds());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        final Spinner roundsSpinner = (Spinner) findViewById(R.id.date_spinner);

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

        ArrayAdapter<CharSequence> countrySpinAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item);
        countrySpinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        try {
            for (Round round : roundManager.getRounds()) {
                countrySpinAdapter.add(round.getDateString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        roundsSpinner.setAdapter(countrySpinAdapter);

        try {
            Integer roundNumber = roundManager.getRound().getNumber();
            roundsSpinner.setSelection(roundNumber != null ? roundNumber - 1 : 1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        roundsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                try {
                    roundManager.setRound(rounds.get(position));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}
