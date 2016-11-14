/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.List;

import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.R;
import eu.motogymkhana.competition.activity.MainActivity;
import eu.motogymkhana.competition.adapter.RiderTimeInputListAdapter;
import eu.motogymkhana.competition.dao.CredentialDao;
import eu.motogymkhana.competition.log.MyLog;
import eu.motogymkhana.competition.model.Rider;
import eu.motogymkhana.competition.notify.Notifier;
import eu.motogymkhana.competition.prefs.MyPreferences;
import eu.motogymkhana.competition.rider.RiderManager;
import eu.motogymkhana.competition.round.RoundManager;
import toothpick.Scope;
import toothpick.Toothpick;

/**
 * created by Christine
 * Fragment 1, or screen 1 in the viewpager
 * this fragment contains the start list, and in admin mode contains links to access the rider time input activity.
 */
public class RiderTimeInputFragment extends ListFragment {

    @Inject
    protected MyPreferences prefs;

    @Inject
    protected RiderManager riderManager;

    @Inject
    protected RoundManager roundManager;

    @Inject
    protected CredentialDao credentialDao;

    @Inject
    protected Notifier notifier;

    @Inject
    protected MyLog log;

    private RiderTimeInputListAdapter adapter;

    private volatile boolean resultSorted = false;

    private List<Rider> riders = new ArrayList<Rider>();
    private volatile boolean attached;
    private Scope scope;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case MainActivity.RIDERTIMES:
                adapter.onActivityResult(resultCode, data);
                break;
        }

    }

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
        titleView.setText(R.string.start_list);

        resultSorted = prefs.isResultSorted();

        adapter = new RiderTimeInputListAdapter(getActivity(), notifier, riderManager);

        setListAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
