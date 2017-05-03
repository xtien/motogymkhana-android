/*
 * Copyright (c) 2015 - 2017, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import eu.motogymkhana.competition.R;
import eu.motogymkhana.competition.model.Rider;
import eu.motogymkhana.competition.model.RiderNameComparator;

/**
 * Created by christine on 19-3-17.
 */

public class AllRiderListAdapter extends BaseAdapter {

    private final LayoutInflater inflater;
    List<Rider> riders = new ArrayList<Rider>();
    private List<Rider> selectedRiders = new ArrayList<Rider>();

    public AllRiderListAdapter(Context context, List<Rider> riders) {
        this.riders=riders;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

        Rider rider = riders.get(position);

        convertView = inflater.inflate(R.layout.all_riders_list_row, null);
        ((TextView) convertView.findViewById(R.id.first_name)).setText(rider.getFirstName());
        ((TextView) convertView.findViewById(R.id.last_name)).setText(rider.getLastName());
        ((TextView) convertView.findViewById(R.id.country)).setText(rider.getCountry().toString());
        if(selectedRiders.contains(rider)){
            ((LinearLayout)convertView.findViewById(R.id.background)).setBackgroundResource(R.color.selected);
        }

        return convertView;
    }

    public void setSelected(Rider rider) {
        selectedRiders.add(rider);
     }
}
