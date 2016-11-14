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

import javax.inject.Inject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.R;
import eu.motogymkhana.competition.adapter.RiderRegistrationListAdapter;
import eu.motogymkhana.competition.dao.RoundDao;
import eu.motogymkhana.competition.dao.TimesDao;
import eu.motogymkhana.competition.model.Rider;
import eu.motogymkhana.competition.notify.Notifier;
import eu.motogymkhana.competition.prefs.MyPreferences;
import eu.motogymkhana.competition.rider.RiderManager;
import eu.motogymkhana.competition.round.RoundManager;
import toothpick.Scope;
import toothpick.Toothpick;

/**
 * Created by Christine
 * Fragment 0, or screen 0, in the viewpager. Only for admin mode.
 * Rider registration means regestring a rider for participation in a race on a particular date. A rider
 * only shows up in the start list if they have registered. A rider can be licensed for a season, which
 * makes them automatically appear in this fragment, or they can be added as a "day rider". A "day rider"
 * can participate in a round, but won't collect points for bibs or season totals.
 */
public class RiderRegistrationFragment extends ListFragment {

    private List<Rider> riders = new ArrayList<Rider>();

    @Inject
    protected RiderManager riderManager;

    @Inject
    protected RoundManager roundManager;

    @Inject
    protected Notifier notifier;

    @Inject
    protected MyPreferences prefs;

    @Inject
    protected TimesDao timesDao;

    private RiderRegistrationListAdapter adapter;
    private volatile boolean attached;
    private Scope scope;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        scope = Toothpick.openScopes(Constants.DEFAULT_SCOPE, this);
        super.onCreate(savedInstanceState);
        Toothpick.inject(this, scope);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView titleView = ((TextView) view.findViewById(R.id.title));
        titleView.setVisibility(View.VISIBLE);
        titleView.setText(R.string.registration);

        adapter = new RiderRegistrationListAdapter(getActivity(), riderManager, notifier);
        adapter.setRegistration();
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

    @Override
    public void onDestroy() {
        Toothpick.closeScope(this);
        super.onDestroy();
    }
}
