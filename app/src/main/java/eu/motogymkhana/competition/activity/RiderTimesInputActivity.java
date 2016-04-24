package eu.motogymkhana.competition.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.inject.Inject;

import java.sql.SQLException;

import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.R;
import eu.motogymkhana.competition.model.Rider;
import eu.motogymkhana.competition.model.Times;
import eu.motogymkhana.competition.rider.RiderManager;
import eu.motogymkhana.competition.rider.UpdateRiderCallback;
import eu.motogymkhana.competition.round.RoundManager;
import eu.motogymkhana.competition.view.PlusMinusView;
import roboguice.RoboGuice;

public class RiderTimesInputActivity extends BaseActivity {

    public static final String RIDER_NUMBER = "rider_number";
    public static final String FOCUS = "focus";

    private int riderNumber;

     private int focus;

    @Inject
    private RiderManager riderManager;

    @Inject
    private RoundManager roundManager;

    Rider rider = null;
    Times riderTimes = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_rider_times_input);
        RoboGuice.getInjector(this).injectMembers(this);

        riderNumber = getIntent().getIntExtra(RIDER_NUMBER,0);
        focus = getIntent().getIntExtra(FOCUS,0);

        ((TextView) findViewById(R.id.date)).setText(Constants.dateFormat.format(roundManager.getDate()));

        try {

            rider = riderManager.getRiderByNumber(riderNumber);
            riderTimes = rider.getEUTimes(roundManager.getDate());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        final EditText time1 = (EditText) findViewById(R.id.time1);
        final EditText time2 = (EditText) findViewById(R.id.time2);
        final PlusMinusView penalties1 = (PlusMinusView) findViewById(R.id.penalties1);
        final PlusMinusView penalties2 = (PlusMinusView) findViewById(R.id.penalties2);
        final CheckBox disqualified1 = (CheckBox) findViewById(R.id.disqualify1);
        final CheckBox disqualified2 = (CheckBox) findViewById(R.id.disqualify2);

        if (rider != null) {

            ((TextView) findViewById(R.id.rider_name)).setText(rider.getName());

            if (riderTimes != null) {

                if (riderTimes.getTime1() != 0) {
                    time1.setText(riderTimes.getTime1String());
                }
                if (riderTimes.getPenalties1() != 0) {
                    penalties1.setNumber(riderTimes.getPenalties1());
                }
                disqualified1.setChecked(riderTimes.isDisqualified1());

                if (riderTimes.isDisqualified1() || riderTimes.getTime1() != 0) {
                    focus = 1;
                }

                if (riderTimes.getTime2() != 0) {
                    time2.setText(riderTimes.getTime2String());
                }
                if (riderTimes.getPenalties2() != 0) {
                    penalties2.setNumber(riderTimes.getPenalties2());
                }
                disqualified2.setChecked(riderTimes.isDisqualified2());
            }

            switch (focus) {

                case 0:
                    time1.requestFocus();
                    break;

                case 1:
                    time2.requestFocus();
                    break;

                default:
                    time1.requestFocus();
            }
        }

        ((Button) findViewById(R.id.okButton)).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (rider != null && riderTimes != null) {

                    riderTimes.setPenalties1(penalties1.getNumber());
                    riderTimes.setPenalties2(penalties2.getNumber());

                    riderTimes.setTime1(time1.getText().toString());
                    riderTimes.setTime2(time2.getText().toString());

                    riderTimes.setDisqualified1(disqualified1.isChecked());
                    riderTimes.setDisqualified2(disqualified2.isChecked());

                    riderManager.update(rider, new UpdateRiderCallback() {

                        @Override
                        public void onSuccess() {
                            riderManager.notifyDataChanged();
                        }

                        @Override
                        public void onError(String error) {
                            Toast.makeText(RiderTimesInputActivity.this, error, Toast.LENGTH_LONG).show();
                        }
                    });
                }

                finish();
            }
        });

    }
}
