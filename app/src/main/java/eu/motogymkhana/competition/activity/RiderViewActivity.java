package eu.motogymkhana.competition.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.inject.Inject;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang.StringUtils;

import java.sql.SQLException;

import eu.motogymkhana.competition.R;
import eu.motogymkhana.competition.api.impl.RidersCallback;
import eu.motogymkhana.competition.model.Bib;
import eu.motogymkhana.competition.model.Country;
import eu.motogymkhana.competition.model.Gender;
import eu.motogymkhana.competition.model.Rider;
import eu.motogymkhana.competition.rider.RiderManager;
import eu.motogymkhana.competition.rider.UpdateRiderCallback;
import roboguice.activity.RoboActivity;

public class RiderViewActivity extends RoboActivity {

    public static final String RIDER_NUMBER = "rider_number";
    public static final String FOCUS = "focus";

    @Inject
    private RiderManager riderManager;
    private int number = 99;

    Rider rider = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_rider);

        final TextView firstNameView = (TextView) findViewById(R.id.first_name);
        final TextView lastNameView = (TextView) findViewById(R.id.last_name);
        final TextView numberView = (TextView) findViewById(R.id.number);
        final TextView countryView = (TextView) findViewById(R.id.country);
        final TextView bibView = (TextView) findViewById(R.id.bib);
        final TextView genderView = (TextView) findViewById(R.id.gender);
        numberView.setText(Integer.toString(number));
        final TextView errorText = (TextView) findViewById(R.id.error_text);
        final int riderNumber = getIntent().getIntExtra(RIDER_NUMBER, -1);
        final TextView bikeView = (TextView) findViewById(R.id.bike);
        final TextView riderTextView = (TextView) findViewById(R.id.rider_text);

        final ImageView riderImage = (ImageView) findViewById(R.id.rider_image);
        final ImageView bikeImage = (ImageView) findViewById(R.id.bike_image);

        if (riderNumber >= 0) {

            try {
                rider = riderManager.getRider(riderNumber);

                firstNameView.setText(rider.getFirstName());
                lastNameView.setText(rider.getLastName());
                numberView.setText(rider.getRiderNumberString());
                genderView.setText(rider.getGender().name());
                bikeView.setText(rider.getBike());
                riderTextView.setText(rider.getText());

                if (rider.hasImageUrl()) {
                    Picasso.with(this).load(rider.getImageUrl()).into(riderImage);
                    riderImage.setVisibility(View.VISIBLE);
                }
                if (rider.hasBikeImageUrl()) {
                    Picasso.with(this).load(rider.getBikeImageUrl()).into(bikeImage);
                    bikeImage.setVisibility(View.VISIBLE);
                }

                countryView.setText(rider.getNationality().name());

                bibView.setText(rider.getBib().name());

            } catch (SQLException e) {
                e.printStackTrace();
                errorText.setText(e.getMessage());
            }
        }
    }
}
