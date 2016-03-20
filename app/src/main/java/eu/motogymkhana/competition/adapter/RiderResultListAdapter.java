package eu.motogymkhana.competition.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import eu.motogymkhana.competition.R;
import eu.motogymkhana.competition.model.Gender;
import eu.motogymkhana.competition.model.Rider;
import eu.motogymkhana.competition.model.RiderBestTimeComparator;
import eu.motogymkhana.competition.model.Times;
import eu.motogymkhana.competition.rider.GetRidersCallback;
import eu.motogymkhana.competition.rider.RiderManager;
import eu.motogymkhana.competition.round.RoundManager;

//import javax.annotation.Nullable;

public class RiderResultListAdapter extends BaseAdapter {

    protected static final int RIDERTIMES = 101;

    private List<Rider> riders = new ArrayList<Rider>();
    private LayoutInflater inflater;
    private volatile boolean sorted = false;
    private volatile boolean registration = false;
    private volatile boolean result = false;

    private Activity activity;

    private RiderManager riderManager;
    private RoundManager roundManager;
    private TextView messageTextView;

    GetRidersCallback callback = new GetRidersCallback() {

        @Override
        public void onSuccess(Collection<Rider> collection) {
            setRiders(collection);
        }

        @Override
        public void onError(String error) {

        }
    };

    ChangeListener changeListener = new ChangeListener() {

        @Override
        public void notifyDataChanged() {

            riderManager.getRiders(callback);
        }
    };

    @Inject
    public RiderResultListAdapter(Context context, final RiderManager riderManager, RoundManager roundManager) {

        this.roundManager = roundManager;
        this.riderManager = riderManager;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        riderManager.registerRiderResultListener(changeListener);

        riderManager.getRiders(callback);
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

        convertView.findViewById(R.id.ranking_view).setVisibility(View.VISIBLE);
        ((TextView) convertView.findViewById(R.id.rankingnumber)).setText(Integer.toString(position + 1));

        ((TextView) convertView.findViewById(R.id.first_name)).setText(rider.getFirstName());
        ((TextView) convertView.findViewById(R.id.last_name)).setText(rider.getLastName());
        ((LinearLayout) convertView.findViewById(R.id.ridernumber_layout)).setVisibility(View.GONE);
        ((TextView) convertView.findViewById(R.id.gender)).setText(rider.getGender() == Gender.F ? "F" : "");
        ((TextView) convertView.findViewById(R.id.bib)).setText(rider.getBib().displayString());
        ((TextView) convertView.findViewById(R.id.nationality)).setText(rider.getNationality().toString());

        TextView timeView1 = (TextView) convertView.findViewById(R.id.time1);
        TextView timeView2 = (TextView) convertView.findViewById(R.id.time2);

        timeView2.setVisibility(View.GONE);
        timeView1.setVisibility(View.VISIBLE);

        if (riderTimes != null && riderTimes.getBestTime() != 0) {
            timeView1.setText(riderTimes.getBestTimeString());
        }

        return convertView;
    }

    public void setRiders(Collection<Rider> riders) {

        this.riders.clear();
        long roundDate = roundManager.getDate();

        if (riders != null) {
            Iterator<Rider> iterator = riders.iterator();
            while (iterator.hasNext()) {
                Rider rider = iterator.next();


                if (rider.hasTimes(roundDate)) {
                    this.riders.add(rider);
                }
            }
        }

        Collections.sort(this.riders, new RiderBestTimeComparator());

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
        RiderResultListAdapter create();
    }
}
