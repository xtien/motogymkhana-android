package eu.motogymkhana.competition.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.inject.Inject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import eu.motogymkhana.competition.R;
import eu.motogymkhana.competition.adapter.ChangeListener;
import eu.motogymkhana.competition.adapter.TotalsListAdapter;
import eu.motogymkhana.competition.adapter.TotalsListAdapter.Factory;
import eu.motogymkhana.competition.model.Rider;
import eu.motogymkhana.competition.rider.RiderManager;
import roboguice.fragment.RoboListFragment;

public class SeasonTotalsFragment extends RoboListFragment {

    @Inject
    private Factory totalsAdapterFactory;

    private List<Rider> riders = new ArrayList<Rider>();
    ;

    @Inject
    private RiderManager riderManager;

    private TotalsListAdapter adapter;
    private volatile boolean attached;

    private ChangeListener riderResultListener = new ChangeListener() {

        @Override
        public void notifyDataChanged() {

            if (attached) {
                try {
                    riderManager.getTotals(adapter);
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    };

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

        adapter = totalsAdapterFactory.create(riders);
        setListAdapter(adapter);

        riderManager.registerRiderResultListener(riderResultListener);

        try {
            riderManager.getTotals(adapter);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        attached = true;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        attached = false;
    }
}
