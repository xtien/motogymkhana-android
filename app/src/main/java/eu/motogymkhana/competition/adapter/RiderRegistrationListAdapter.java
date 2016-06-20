/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.inject.Inject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import eu.motogymkhana.competition.R;
import eu.motogymkhana.competition.activity.RiderNewUpdateActivity;
import eu.motogymkhana.competition.activity.RiderViewActivity;
import eu.motogymkhana.competition.dao.TimesDao;
import eu.motogymkhana.competition.model.Bib;
import eu.motogymkhana.competition.model.Gender;
import eu.motogymkhana.competition.model.Rider;
import eu.motogymkhana.competition.model.RiderNumberComparator;
import eu.motogymkhana.competition.model.Times;
import eu.motogymkhana.competition.rider.GetRidersCallback;
import eu.motogymkhana.competition.rider.RiderManager;
import eu.motogymkhana.competition.rider.UpdateRiderCallback;
import eu.motogymkhana.competition.round.RoundManager;

/**
 * created by Christine
 * adapter for the rider registration list fragment
 */
public class RiderRegistrationListAdapter extends BaseAdapter {

    protected static final int RIDERTIMES = 101;

    private List<Rider> riders = new ArrayList<Rider>();
    private LayoutInflater inflater;
    private volatile boolean sorted = false;
    private volatile boolean registration = false;
    private volatile boolean result = false;

    private Activity activity;

    private RiderManager riderManager;
    private RoundManager roundManager;

    private TimesDao timesDao;

    private ChangeListener changeListener = new ChangeListener() {

        @Override
        public void notifyDataChanged() {

            riderManager.getRiders(callback);
        }
    };

    GetRidersCallback callback = new GetRidersCallback() {

        @Override
        public void onSuccess(Collection<Rider> collection) {
            setRiders(collection);
        }

        @Override
        public void onError(String error) {

        }
    };

    @Inject
    public RiderRegistrationListAdapter(Context context, final RiderManager riderManager, RoundManager roundManager,
                                        TimesDao timesDao) {

        this.riderManager = riderManager;
        this.roundManager = roundManager;
        this.timesDao = timesDao;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        riderManager.getRiders(callback);
        riderManager.registerRiderResultListener(changeListener);
    }

    @Override
    public int getCount() {
        return riders.size();
    }

    @Override
    public Object getItem(int position) {
        return riders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        convertView = (LinearLayout) inflater.inflate(R.layout.rider_registration_list_row, null);

        long roundDate = roundManager.getDate();

        final Rider rider = riders.get(position);
        Times riderTimes = rider.getEUTimes(roundDate);

        if (riderTimes == null) {
            riderTimes = new Times(roundDate);
            riderTimes.setRider(rider);
            riderTimes.setDate(roundDate);
            rider.addTimes(riderTimes);
        }

        ((LinearLayout) convertView.findViewById(R.id.rider_layout)).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(activity, RiderViewActivity.class);
                intent.putExtra(RiderViewActivity.RIDER_NUMBER, rider.getRiderNumber());

                activity.startActivity(intent);
            }
        });

        ((TextView) convertView.findViewById(R.id.first_name)).setText(rider.getFirstName());
        ((TextView) convertView.findViewById(R.id.last_name)).setText(rider.getLastName());
        ((TextView) convertView.findViewById(R.id.ridernumber)).setText(rider.getRiderNumberString());

        if (rider.getCountry() != null) {
            ((TextView) convertView.findViewById(R.id.country)).setText(rider.getNationality().name());
        }

        if (rider.getBib() == null) {
            rider.setBib(Bib.Y);
        }

        TextView startNumber = (TextView) convertView.findViewById(R.id.startnumber);
        TextView editView = ((TextView) convertView.findViewById(R.id.edit));

        startNumber.setText(Integer.toString(riderTimes.getStartNumber()));
        startNumber.setBackgroundColor(rider.getBibColor());

        editView.setVisibility(View.VISIBLE);
        editView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(activity, RiderNewUpdateActivity.class);
                intent.putExtra(RiderNewUpdateActivity.RIDER_NUMBER, rider.getRiderNumber());
                activity.startActivity(intent);
            }
        });

        CheckBox isDayRider = (CheckBox) convertView.findViewById(R.id.day_rider_box);
        isDayRider.setVisibility(View.VISIBLE);
        isDayRider.setChecked(rider.isDayRider());

        ((TextView) convertView.findViewById(R.id.gender)).setText(rider.getGender() == Gender.F ? "F" : "");

        CheckBox isRegistered = (CheckBox) convertView.findViewById(R.id.registered_box);
        isRegistered.setVisibility(View.VISIBLE);
        isRegistered.setChecked(riderTimes.isRegistered());

        final Times finalRiderTimes = riderTimes;
        isRegistered.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                try {
                    riderManager.setRegistered(finalRiderTimes, isChecked);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        isDayRider.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                rider.setDayRider(isChecked);

                riderManager.update(rider, new UpdateRiderCallback() {

                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(String error) {

                    }
                });
            }
        });

        return convertView;
    }

    public void setRiders(Collection<Rider> riders) {

        if (riders != null) {
            this.riders.clear();
            this.riders.addAll(riders);
        } else {
            this.riders = new ArrayList<Rider>();
        }

        if (this.riders.size() > 0) {
            Collections.sort(this.riders, new RiderNumberComparator());
        }

        notifyDataSetChanged();
    }

    public void setSorted() {
        sorted = true;
        notifyDataSetChanged();
    }

    public void setRegistration() {
        registration = true;
        notifyDataSetChanged();
    }

    public void setResult() {
        result = true;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
