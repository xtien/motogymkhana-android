/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.List;

import eu.motogymkhana.competition.R;
import eu.motogymkhana.competition.adapter.RiderRegistrationListAdapter;
import eu.motogymkhana.competition.dao.RoundDao;
import eu.motogymkhana.competition.dao.TimesDao;
import eu.motogymkhana.competition.model.Rider;
import eu.motogymkhana.competition.rider.RiderManager;
import eu.motogymkhana.competition.round.RoundManager;
import roboguice.RoboGuice;

/**
 * Created by Christine
 * Rider registration means regestring a rider for participation in a race on a particular date. A rider
 * only shows up in the start list if they have registered. A rider can be licensed for a season, which
 * makes them automatically appear in this fragment, or they can be added as a "day rider". A "day rider"
 * can participate in a round, but won't collect points for bibs or season totals.
 */
public class RiderRegistrationFragment extends ListFragment {

    private List<Rider> riders = new ArrayList<Rider>();

    @Inject
    private RiderManager riderManager;

    @Inject
    private RoundManager roundManager;

    @Inject
    private TimesDao timesDao;

    private RiderRegistrationListAdapter adapter;
    private volatile boolean attached;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RoboGuice.getInjector(getActivity()).injectMembers(this);

        TextView titleView = ((TextView) view.findViewById(R.id.title));
        titleView.setVisibility(View.VISIBLE);
        titleView.setText(R.string.registration);

        adapter = new RiderRegistrationListAdapter( getActivity(),  riderManager,  roundManager,
                 timesDao);
        adapter.setRegistration();
        adapter.setActivity(getActivity());
        setListAdapter(adapter);
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        attached = true;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        attached = false;
    }
}
