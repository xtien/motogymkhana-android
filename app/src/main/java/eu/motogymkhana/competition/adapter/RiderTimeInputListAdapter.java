package eu.motogymkhana.competition.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import eu.motogymkhana.competition.R;
import eu.motogymkhana.competition.activity.RiderTimesInputActivity;
import eu.motogymkhana.competition.model.Rider;
import eu.motogymkhana.competition.model.RiderStartNumberComparator;
import eu.motogymkhana.competition.model.Times;
import eu.motogymkhana.competition.prefs.ChristinePreferences;
import eu.motogymkhana.competition.rider.GetRidersCallback;
import eu.motogymkhana.competition.rider.RiderManager;
import eu.motogymkhana.competition.round.RoundManager;

//import javax.annotation.Nullable;

public class RiderTimeInputListAdapter extends BaseAdapter {

    protected static final int RIDERTIMES = 101;
    private final ChristinePreferences prefs;

    private List<Rider> riders = new ArrayList<Rider>();
    private LayoutInflater inflater;
    private volatile boolean sorted = false;
    private volatile boolean registration = false;
    private volatile boolean result = false;

    private Activity activity;

    private RiderManager riderManager;
    private RoundManager roundManager;

    private final ChangeListener changeListener = new ChangeListener() {

        @Override
        public void notifyDataChanged() {

            riderManager.getRegisteredRiders(callback);

        }
    };

    GetRidersCallback callback = new GetRidersCallback() {

        @Override
        public void onSuccess(Collection<Rider> collection) {
            setRiders(collection);
        }

        @Override
        public void onError(String error) {

        }
    };

    @Inject
    public RiderTimeInputListAdapter(Context context, final RiderManager riderManager,final RoundManager roundManager, final ChristinePreferences prefs) {

        this.riderManager = riderManager;
        this.roundManager = roundManager;
        this.prefs = prefs;
        riderManager.registerRiderResultListener(changeListener);

        riderManager.getRegisteredRiders(callback);

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

        convertView = (LinearLayout) inflater.inflate(R.layout.rider_list_row, null);

        final Rider rider = riders.get(position);
        final Times riderTimes = rider.getEUTimes(roundManager.getDate());

        ((TextView) convertView.findViewById(R.id.first_name)).setText(rider.getFirstName() + " " + rider.getLastName());
        ((TextView) convertView.findViewById(R.id.last_name)).setVisibility(View.GONE);
        ((TextView) convertView.findViewById(R.id.ridernumber)).setText(riderTimes.getStartNumberString());
        ((TextView) convertView.findViewById(R.id.gender)).setVisibility(View.GONE);

        TextView timeView1 = ((TextView) convertView.findViewById(R.id.time1));
        TextView timeView2 = ((TextView) convertView.findViewById(R.id.time2));

        timeView1.setVisibility(View.VISIBLE);
        timeView2.setVisibility(View.VISIBLE);

        if (riderTimes.isDisqualified1()) {
            timeView1.setBackgroundColor(activity.getResources().getColor(R.color.disqualified));
        } else {
            timeView1.setBackgroundColor(activity.getResources().getColor(R.color.qualified));
            if (riderTimes.getTime1() != 0) {
                timeView1.setText(riderTimes.getTime1PlusPenaltiesString());
            }
        }

        if (riderTimes.isDisqualified2()) {
            timeView2.setBackgroundColor(activity.getResources().getColor(R.color.disqualified));
        } else {
            timeView2.setBackgroundColor(activity.getResources().getColor(R.color.qualified));
            if (riderTimes.getTime2() != 0) {
                timeView2.setText(riderTimes.getTime2PlusPenaltiesString());
            }
        }

        if (prefs.isAdmin()) {
            timeView1.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(activity, RiderTimesInputActivity.class);
                    intent.putExtra(RiderTimesInputActivity.RIDER_NUMBER, rider.getRiderNumber());
                    intent.putExtra(RiderTimesInputActivity.FOCUS, 0);
                    activity.startActivityForResult(intent, RIDERTIMES);
                }
            });

            timeView2.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(activity, RiderTimesInputActivity.class);
                    intent.putExtra(RiderTimesInputActivity.RIDER_NUMBER, rider.getRiderNumber());
                    intent.putExtra(RiderTimesInputActivity.FOCUS, 0);
                    activity.startActivityForResult(intent, RIDERTIMES);
                }
            });
        }

        return convertView;
    }

    public void setRiders(Collection<Rider> collection) {

        if (riders != null) {
            this.riders.clear();
            this.riders.addAll(collection);
            Collections.sort(riders, new RiderStartNumberComparator());
        } else {
            this.riders = new ArrayList<Rider>();
        }

        notifyDataSetChanged();
    }

    public void setSorted() {
        sorted = true;
        notifyDataSetChanged();
    }

    public void setRegistration() {
        registration = true;
        notifyDataSetChanged();
    }

    public void setResult() {
        result = true;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public interface Factory {
        RiderTimeInputListAdapter create();
    }
}
