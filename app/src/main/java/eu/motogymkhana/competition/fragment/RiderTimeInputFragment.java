package eu.motogymkhana.competition.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.List;

import eu.motogymkhana.competition.R;
import eu.motogymkhana.competition.activity.RiderViewActivity;
import eu.motogymkhana.competition.adapter.RiderTimeInputListAdapter;
import eu.motogymkhana.competition.model.Rider;
import eu.motogymkhana.competition.prefs.ChristinePreferences;
import eu.motogymkhana.competition.rider.RiderManager;
import roboguice.fragment.RoboListFragment;

public class RiderTimeInputFragment extends RoboListFragment {

    @Inject
    private RiderTimeInputListAdapter.Factory riderListAdapterFactory;

    @Inject
    private ChristinePreferences prefs;

    @Inject
    private RiderManager riderManager;

    private RiderTimeInputListAdapter adapter;

    private volatile boolean resultSorted = false;

    private List<Rider> riders = new ArrayList<Rider>();
    private volatile boolean attached;

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

        adapter = riderListAdapterFactory.create();
        adapter.setSorted();
        adapter.setActivity(getActivity());

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

}
