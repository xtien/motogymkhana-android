/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.R;
import eu.motogymkhana.competition.dao.RoundDao;
import eu.motogymkhana.competition.model.Rider;
import eu.motogymkhana.competition.model.Round;
import eu.motogymkhana.competition.rider.RiderManager;
import eu.motogymkhana.competition.settings.SettingsManager;
import toothpick.Scope;
import toothpick.Toothpick;

/**
 * created by Christine
 * adapter for the totals list fragment
 */
public class TotalsListAdapter extends BaseAdapter {

    protected static final int RIDERTIMES = 101;
    private final Activity activity;
    private final Scope scope;

    private List<Rider> riders = new ArrayList<Rider>();
    private LayoutInflater inflater;

    @Inject
    protected RiderManager riderManager;

    @Inject
    protected RoundDao roundDao;

    @Inject
    protected SettingsManager settingsManager;

    public TotalsListAdapter(Activity activity, Collection<Rider> riders) {

        scope = Toothpick.openScopes(Constants.DEFAULT_SCOPE, this);
        Toothpick.inject(this, scope);

        this.activity = activity;

        try {
            Collection<Round> rounds = roundDao.getRounds();
            if (rounds != null && rounds.size() > 1) {
                if (riders != null && riders.size() > 0) {
                    this.riders.clear();
                    this.riders.addAll(riders);
                } else {
                    this.riders.clear();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
    public View getView(int position, View convertView, ViewGroup parent) {

        int roundsCountingForSeasonResult = settingsManager.getRoundsCountingForSeasonResult();

        convertView = (LinearLayout) inflater.inflate(R.layout.rider_list_row, null);

        final Rider rider = riders.get(position);

        ((TextView) convertView.findViewById(R.id.first_name)).setText(rider.getFirstName());
        ((TextView) convertView.findViewById(R.id.last_name)).setText(rider.getLastName());
        TextView riderNumberView = (TextView) convertView.findViewById(R.id.ridernumber);
        riderNumberView.setText("" + (position + 1));
        riderNumberView.setBackgroundColor(rider.getBibColor());

        ((TextView) convertView.findViewById(R.id.nationality)).setText(rider.getNationality().toString());

        TextView timeView1 = ((TextView) convertView.findViewById(R.id.time1));
        TextView timeView2 = ((TextView) convertView.findViewById(R.id.time2));

        timeView2.setVisibility(View.GONE);
        timeView1.setVisibility(View.VISIBLE);
        if (rider.getTotalPoints(roundsCountingForSeasonResult) != 0) {
            timeView1.setText(rider.getTotalPointsString(roundsCountingForSeasonResult));
        }

        return convertView;
    }

    public void setRiders(final List<Rider> riders) {

        if (riders != null) {

            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    TotalsListAdapter.this.riders = riders;
                    notifyDataSetChanged();
                }
            });
        }
    }
}
