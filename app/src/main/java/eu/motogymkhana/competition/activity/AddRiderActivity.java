/*
 * Copyright (c) 2015 - 2017, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.activity;

import android.app.MediaRouteButton;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.RunnableFuture;

import javax.inject.Inject;

import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.R;
import eu.motogymkhana.competition.adapter.AllRiderListAdapter;
import eu.motogymkhana.competition.api.ApiManager;
import eu.motogymkhana.competition.api.ResponseHandler;
import eu.motogymkhana.competition.api.response.ListRidersResult;
import eu.motogymkhana.competition.api.response.UpdateRiderResponse;
import eu.motogymkhana.competition.dao.RiderDao;
import eu.motogymkhana.competition.model.Registration;
import eu.motogymkhana.competition.model.Rider;
import eu.motogymkhana.competition.model.RiderNameComparator;
import eu.motogymkhana.competition.model.Times;
import eu.motogymkhana.competition.notify.Notifier;
import eu.motogymkhana.competition.rider.GetRidersCallback;
import eu.motogymkhana.competition.rider.RiderManager;
import eu.motogymkhana.competition.round.RoundManager;
import toothpick.Scope;
import toothpick.Toothpick;

/**
 * Created by christine on 19-3-17.
 */

public class AddRiderActivity extends BaseActivity {

    private static final String LOGTAG = AddRiderActivity.class.getSimpleName();
    private Scope scope;

    private ListView listView;

    private List<Rider> riders = new ArrayList<Rider>();
    private TextView emptyView;

    @Inject
    protected RiderManager riderManager;

    @Inject
    protected RoundManager roundManager;

    @Inject
    protected RiderDao riderDao;

    @Inject
    protected Notifier notifier;

    private AllRiderListAdapter adapter;
    private ProgressBar progressBar;

    private ResponseHandler updateResponseHandler = new ResponseHandler() {

        @Override
        public void onSuccess(Object object) {
            UpdateRiderResponse response = (UpdateRiderResponse) object;
            Rider rider = response.getRider();
            if (rider != null) {
                try {
                    riderDao.store(rider);
                } catch (SQLException e) {
                    onException(e);
                }

                adapter.setSelected(rider);

                Log.d(LOGTAG, "what?");
                notifier.notifyDataChanged();
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                }
            });
        }

        @Override
        public void onException(Exception e) {
            e.printStackTrace();
        }

        @Override
        public void onError(int statusCode, String string) {

        }
    };

    private AdapterView.OnItemClickListener clickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            progressBar.setVisibility(View.VISIBLE);
            Rider rider = riders.get(position);
            rider.addRegistration(new Registration(Constants.country, Constants.season, true));
            riderManager.update(rider, updateResponseHandler);
        }
    };

    private ResponseHandler responseHandler = new ResponseHandler() {

        @Override
        public void onSuccess(Object object) {

            ListRidersResult result = (ListRidersResult) object;
            riders.clear();

            if (result.getRiders() != null && result.getRiders().size() > 0) {
                for (Rider r : result.getRiders()) {
                    if (!r.hasRegistration()) {
                        riders.add(r);
                    }
                }
                Collections.sort(riders, new RiderNameComparator(true));

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        emptyView.setVisibility(View.GONE);
                        adapter = new AllRiderListAdapter(getApplicationContext(), riders);
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener(clickListener);
                    }
                });
            }
        }

        @Override
        public void onException(Exception e) {

        }

        @Override
        public void onError(int statusCode, String string) {

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {

        scope = Toothpick.openScopes(Constants.DEFAULT_SCOPE, this);
        super.onCreate(savedInstanceState);
        Toothpick.inject(this, scope);

        setContentView(R.layout.activity_add_rider);

        listView = (ListView) findViewById(R.id.riders_list);
        emptyView = (TextView) findViewById(R.id.empty);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        riderManager.getAllRiders(responseHandler);
    }
}
