/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.activity;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import eu.motogymkhana.competition.model.Bib;

/**
 * Created by christine on 4-7-16.
 */
public class BibSpinAdapter extends BaseAdapter {

    private final Context context;

    public BibSpinAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return Bib.values().length;
    }

    @Override
    public Object getItem(int position) {
        return Bib.values()[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LinearLayout layout = new LinearLayout(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100,60);
        layout.setLayoutParams(params);
         layout.setBackgroundColor(Bib.values()[position].getColor());

        return layout;
    }
}
