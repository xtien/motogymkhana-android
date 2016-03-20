package eu.motogymkhana.competition.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.List;

import eu.motogymkhana.competition.R;
import eu.motogymkhana.competition.adapter.RiderRegistrationListAdapter;
import eu.motogymkhana.competition.model.Rider;
import eu.motogymkhana.competition.rider.RiderManager;
import roboguice.fragment.RoboListFragment;

public class RiderRegistrationFragment extends RoboListFragment {

    @Inject
    private RiderRegistrationListAdapter.Factory riderListAdapterFactory;

    private List<Rider> riders = new ArrayList<Rider>();

    @Inject
    private RiderManager riderManager;

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

        TextView titleView = ((TextView) view.findViewById(R.id.title));
        titleView.setVisibility(View.VISIBLE);
        titleView.setText(R.string.registration);

        adapter = riderListAdapterFactory.create(riders);
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
