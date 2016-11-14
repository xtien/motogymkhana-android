/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;

import javax.inject.Inject;

import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.R;
import eu.motogymkhana.competition.adapter.RiderTimeInputListAdapter;
import eu.motogymkhana.competition.api.ApiManager;
import eu.motogymkhana.competition.api.ResponseHandler;
import eu.motogymkhana.competition.api.response.UpdateRiderResponse;
import eu.motogymkhana.competition.log.MyLog;
import eu.motogymkhana.competition.model.Rider;
import eu.motogymkhana.competition.model.Times;
import eu.motogymkhana.competition.notify.Notifier;
import eu.motogymkhana.competition.prefs.MyPreferences;
import eu.motogymkhana.competition.rider.RiderManager;
import eu.motogymkhana.competition.round.RoundManager;
import eu.motogymkhana.competition.view.PlusMinusView;
import toothpick.Scope;
import toothpick.Toothpick;

/**
 * created by Christine
 * Activity for entering times and penalty seconds for a rider.
 */
public class RiderTimesInputActivity extends BaseActivity {

    public static final String RIDER_NUMBER = "rider_number";
    public static final String FOCUS = "focus";
    private static final String LOGTAG = RiderTimesInputActivity.class.getSimpleName();

    private int riderNumber;

    private int focus;

    @Inject
    protected Notifier notifier;

    @Inject
    protected MyPreferences prefs;

    @Inject
    protected MyLog log;

    @Inject
    protected RiderManager riderManager;

    @Inject
    protected ApiManager api;

    @Inject
    protected RoundManager roundManager;

    Rider rider = null;
    Times riderTimes = null;

    private ResponseHandler updateRiderResponseHandler = new ResponseHandler() {

        @Override
        public void onSuccess(Object object) {

            UpdateRiderResponse result = (UpdateRiderResponse) object;

            if (result.isOK()) {
                notifier.notifyDataChanged();
                setResult(RiderTimeInputListAdapter.RIDER_CHANGED);
                finish();
            }
        }

        @Override
        public void onException(Exception e) {
            log.e(LOGTAG, e);
            finish();
        }

        @Override
        public void onError(int statusCode, String error) {
            log.e(LOGTAG, error);
            Toast.makeText(RiderTimesInputActivity.this, error, Toast.LENGTH_LONG).show();
        }
    };
    private Scope scope;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        scope = Toothpick.openScopes(Constants.DEFAULT_SCOPE, this);
        super.onCreate(savedInstanceState);
        Toothpick.inject(this, scope);

        setContentView(R.layout.activity_rider_times_input);

        riderNumber = getIntent().getIntExtra(RIDER_NUMBER, 0);
        focus = getIntent().getIntExtra(FOCUS, 0);

        ((TextView) findViewById(R.id.date)).setText(Constants.dateFormat.format(prefs.getDate()));

        try {

            rider = riderManager.getRiderByNumber(riderNumber);
            riderTimes = rider.getEUTimes(prefs.getDate());

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

                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                View view = getCurrentFocus();
                if (view != null) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                if (rider != null && riderTimes != null) {

                    riderTimes.setPenalties1(penalties1.getNumber());
                    riderTimes.setPenalties2(penalties2.getNumber());

                    riderTimes.setTime1(time1.getText().toString());
                    riderTimes.setTime2(time2.getText().toString());

                    riderTimes.setDisqualified1(disqualified1.isChecked());
                    riderTimes.setDisqualified2(disqualified2.isChecked());

                    riderManager.update(riderTimes, updateRiderResponseHandler);

                } else {
                    finish();
                }
            }
        });
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Toothpick.closeScope(this);
    }
}
