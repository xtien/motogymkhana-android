package eu.motogymkhana.competition.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import eu.motogymkhana.competition.R;
import eu.motogymkhana.competition.model.Round;

/**
 * Created by christine on 6-2-16.
 */
public class ManageRoundsAdapter extends BaseAdapter {

    private final Context context;
    private final LayoutInflater inflater;
    private List<Round> rounds;

    public ManageRoundsAdapter(Context context, List<Round> rounds) {
        this.context = context;
        this.rounds = rounds;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return rounds.size();
    }

    @Override
    public Object getItem(int position) {
        return rounds.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.date_list_row, null);

        ((TextView) convertView.findViewById(R.id.date_string)).setText(rounds.get(position).getDateString());
        ((Button) convertView.findViewById(R.id.delete)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                rounds.remove(position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    public List<Round> getRounds() {
        return rounds;
    }

    public void add(Round round) {
        rounds.add(round);
        notifyDataSetChanged();
    }
}
