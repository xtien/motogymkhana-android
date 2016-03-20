package eu.motogymkhana.competition.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;

import com.google.inject.Inject;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.R;
import eu.motogymkhana.competition.adapter.ManageRoundsAdapter;
import eu.motogymkhana.competition.model.Country;
import eu.motogymkhana.competition.model.Round;
import eu.motogymkhana.competition.prefs.ChristinePreferences;
import eu.motogymkhana.competition.round.RoundManager;

/**
 * Created by christine on 6-2-16.
 */
public class ManageRoundsFragment extends BaseFragment implements DatePickerDialog.OnDateSetListener {

    private ListView listView;
    private Button saveButton;

    @Inject
    private RoundManager roundManager;
    private DatePicker datePicker;
    private Button addButton;
    private ManageRoundsAdapter adapter;
    private ChristinePreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_rounds, container, false);

        listView = (ListView) view.findViewById(R.id.date_list);
        addButton = (Button) view.findViewById(R.id.add);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        List<Round> rounds = null;
        try {
            rounds = roundManager.getRounds();
        } catch (SQLException e) {
            showAlert(e);
        }

        adapter = new ManageRoundsAdapter(getContext(), rounds);
        listView.setAdapter(adapter);

        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DatePickerFragment picker = new DatePickerFragment();
                picker.setListener(ManageRoundsFragment.this);
                picker.show(getFragmentManager(), "datePicker");
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {

        Calendar c = Calendar.getInstance();
        c.set(year, month, day);

        Round round = new Round();
        round.setDate(c.getTimeInMillis());
        round.setTimeStamp(System.currentTimeMillis());
        round.setCountry(Constants.country);
        round.setSeason(Constants.season);

        adapter.add(round);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(c.getTime());
    }

    @Override
    public void onBackPressed() {

        try {
            roundManager.save(adapter.getRounds());
        } catch (SQLException e) {
            showAlert(e);
        }
    }
}
