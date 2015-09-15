package eu.motogymkhana.competition.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import eu.motogymkhana.competition.R;
import eu.motogymkhana.competition.model.Rider;
import eu.motogymkhana.competition.rider.RiderManager;

//import javax.annotation.Nullable;

public class TotalsListAdapter extends BaseAdapter {

	protected static final int RIDERTIMES = 101;

	private List<Rider> riders = new ArrayList<Rider>();
	private LayoutInflater inflater;

	private RiderManager riderManager;

	@Inject
	public TotalsListAdapter(@Assisted @Nullable Collection<Rider> riders, Context context,
			RiderManager riderManager) {

		this.riderManager = riderManager;

		if (riders != null && riders.size() > 0) {
			this.riders.clear();
			this.riders.addAll(riders);
		}
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

		((TextView) convertView.findViewById(R.id.first_name)).setText(rider.getFirstName());
		((TextView) convertView.findViewById(R.id.last_name)).setText(rider.getLastName());
		((TextView) convertView.findViewById(R.id.ridernumber)).setText(rider
				.getRiderNumberString());

		TextView timeView1 = ((TextView) convertView.findViewById(R.id.time1));
		TextView timeView2 = ((TextView) convertView.findViewById(R.id.time2));

		((TextView) convertView.findViewById(R.id.bib)).setText(rider.getBib().displayString());

		timeView2.setVisibility(View.GONE);
		timeView1.setVisibility(View.VISIBLE);
		if (rider.getTotalPoints() != 0) {
			timeView1.setText(rider.getTotalPointsString());
		}

		return convertView;
	}

	public void setRiders(List<Rider> riders) {

		if (riders != null) {
			this.riders = riders;
			notifyDataSetChanged();
		}
	}

	public interface Factory {
		TotalsListAdapter create(@Assisted @Nullable Collection<Rider> list);
	}
}
