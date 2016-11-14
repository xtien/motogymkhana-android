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

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.R;
import eu.motogymkhana.competition.adapter.ChangeListener;
import eu.motogymkhana.competition.adapter.TotalsListAdapter;
import eu.motogymkhana.competition.dao.RoundDao;
import eu.motogymkhana.competition.model.Rider;
import eu.motogymkhana.competition.notify.Notifier;
import eu.motogymkhana.competition.rider.RiderManager;
import toothpick.Scope;
import toothpick.Toothpick;

/**
 * created by Christine
 * Fragment 3, or screen 3, in the viewpager.
 * Season totals contains the total results (sum of points for each round) per rider.
 */
public class SeasonTotalsFragment extends ListFragment {

    private List<Rider> riders = new ArrayList<Rider>();

    @Inject
    protected RiderManager riderManager;

    @Inject
    protected Notifier notifier;

    @Inject
    protected RoundDao roundDao;

    private TotalsListAdapter adapter;
    private volatile boolean attached;
    private TextView emptyView;

    private ChangeListener riderResultListener = new ChangeListener() {

        @Override
        public void notifyDataChanged() {

            if (attached) {
                try {
                    riderManager.getTotals(adapter);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };

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
        titleView.setText(R.string.totals);

        adapter = new TotalsListAdapter(getActivity(), riders);
        setListAdapter(adapter);

        notifier.registerRiderResultListener(riderResultListener);

        try {
            riderManager.getTotals(adapter);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
