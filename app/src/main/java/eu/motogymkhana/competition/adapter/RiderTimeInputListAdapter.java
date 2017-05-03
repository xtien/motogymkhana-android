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

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.R;
import eu.motogymkhana.competition.activity.MainActivity;
import eu.motogymkhana.competition.activity.RiderTimesInputActivity;
import eu.motogymkhana.competition.activity.RiderViewActivity;
import eu.motogymkhana.competition.dao.CredentialDao;
import eu.motogymkhana.competition.log.MyLog;
import eu.motogymkhana.competition.model.Rider;
import eu.motogymkhana.competition.model.RiderStartNumberComparator;
import eu.motogymkhana.competition.model.Times;
import eu.motogymkhana.competition.notify.Notifier;
import eu.motogymkhana.competition.prefs.MyPreferences;
import eu.motogymkhana.competition.rider.GetRidersCallback;
import eu.motogymkhana.competition.rider.RiderManager;
import eu.motogymkhana.competition.round.RoundManager;
import toothpick.Scope;
import toothpick.Toothpick;

//import javax.annotation.Nullable;

public class RiderTimeInputListAdapter extends BaseAdapter {

    public static final int RIDER_CHANGED = 101;
    private static final String LOGTAG = RiderTimeInputListAdapter.class.getSimpleName();
    private  Scope scope;
    private List<Rider> riders = new ArrayList<Rider>();
    private LayoutInflater inflater;

    private Activity activity;

    protected RiderManager riderManager;

    @Inject
    protected RoundManager roundManager;

    @Inject
    protected CredentialDao credentialDao;

    @Inject
    protected MyPreferences prefs;

    private Notifier notifier;

    @Inject
    protected MyLog log;

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
            log.e(LOGTAG, error);
        }
    };

    public RiderTimeInputListAdapter() {

    }

    public RiderTimeInputListAdapter(Activity activity, Notifier notifier, RiderManager riderManager) {

        scope = Toothpick.openScopes(Constants.DEFAULT_SCOPE, this);
        Toothpick.inject(this, scope);

        this.notifier = notifier;
        this.activity = activity;
        this.riderManager = riderManager;

        notifier.registerRiderResultListener(changeListener);

        riderManager.getRegisteredRiders(callback);

        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

        convertView = inflater.inflate(R.layout.rider_list_row, null);

        long date = prefs.getDate();

        final Rider rider = riders.get(position);
        final Times riderTimes = rider.getEUTimes(date);

        convertView.findViewById(R.id.rider_layout).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(activity, RiderViewActivity.class);
                intent.putExtra(RiderViewActivity.RIDER_ID, rider.getRiderId());

                activity.startActivity(intent);
            }
        });

        ((TextView) convertView.findViewById(R.id.first_name)).setText(rider.getFirstName() + " " + rider.getLastName());
        convertView.findViewById(R.id.last_name).setVisibility(View.GONE);
        convertView.findViewById(R.id.gender).setVisibility(View.GONE);

        if (date > 0l && riderTimes != null) {

            TextView riderNumberView = (TextView) convertView.findViewById(R.id.ridernumber);
            LinearLayout riderNumberLayout = (LinearLayout) convertView.findViewById(R.id.ridernumber_layout);
            riderNumberView.setText(riderTimes.getStartNumberString());
            riderNumberLayout.setBackgroundColor(rider.getBibColor());

            ((TextView) convertView.findViewById(R.id.nationality)).setText(rider.getNationality().toString());
            TextView pointsView1 = ((TextView) convertView.findViewById(R.id.points1));
            TextView pointsView2 = ((TextView) convertView.findViewById(R.id.points2));
            if (pointsView1 != null) {
                pointsView1.setText(riderTimes.getPenalties1String());
                pointsView1.setVisibility(View.VISIBLE);
            }
            if (pointsView2 != null) {
                pointsView2.setText(riderTimes.getPenalties2String());
                pointsView2.setVisibility(View.VISIBLE);
            }

            TextView timeView1 = ((TextView) convertView.findViewById(R.id.time1));
            TextView timeView2 = ((TextView) convertView.findViewById(R.id.time2));

            timeView1.setVisibility(View.VISIBLE);
            timeView2.setVisibility(View.VISIBLE);

            if (riderTimes.isDisqualified1()) {
                timeView1.setBackgroundColor(activity.getResources().getColor(R.color.disqualified));
                timeView1.setTextColor(activity.getResources().getColor(R.color.gray));
            } else {
                timeView1.setBackgroundColor(activity.getResources().getColor(R.color.qualified));
            }
            if (riderTimes.getTime1() != 0) {
                timeView1.setText(riderTimes.getTime1PlusPenaltiesString());
            }

            if (riderTimes.isDisqualified2()) {
                timeView2.setBackgroundColor(activity.getResources().getColor(R.color.disqualified));
                timeView2.setTextColor(activity.getResources().getColor(R.color.gray));
            } else {
                timeView2.setBackgroundColor(activity.getResources().getColor(R.color.qualified));
            }
            if (riderTimes.getTime2() != 0) {
                timeView2.setText(riderTimes.getTime2PlusPenaltiesString());
            }

            if (credentialDao.isAdmin()) {
                timeView1.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(activity, RiderTimesInputActivity.class);
                        intent.putExtra(RiderTimesInputActivity.RIDER_ID, rider.getRiderId());
                        intent.putExtra(RiderTimesInputActivity.FOCUS, 0);
                        activity.startActivityForResult(intent, MainActivity.RIDERTIMES);
                    }
                });

                timeView2.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(activity, RiderTimesInputActivity.class);
                        intent.putExtra(RiderTimesInputActivity.RIDER_ID, rider.getRiderId());
                        intent.putExtra(RiderTimesInputActivity.FOCUS, 0);
                        activity.startActivityForResult(intent, MainActivity.RIDERTIMES);
                    }
                });
            }
        }

        return convertView;
    }

    public void setRiders(Collection<Rider> collection) {

        if (riders != null) {
            riders.clear();
            riders.addAll(collection);
            Collections.sort(riders, new RiderStartNumberComparator());
        } else {
            riders = new ArrayList<Rider>();
        }

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    public void onActivityResult(int resultCode, Intent data) {

        if (resultCode == RIDER_CHANGED) {
            riderManager.getRegisteredRiders(callback);
        }
    }
}
