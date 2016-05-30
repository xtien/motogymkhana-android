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

import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.R;
import eu.motogymkhana.competition.api.impl.RidersCallback;
import eu.motogymkhana.competition.model.Bib;
import eu.motogymkhana.competition.model.Country;
import eu.motogymkhana.competition.model.Gender;
import eu.motogymkhana.competition.model.Rider;
import eu.motogymkhana.competition.rider.RiderManager;
import eu.motogymkhana.competition.rider.UpdateRiderCallback;
import roboguice.RoboGuice;

public class RiderNewUpdateActivity extends BaseActivity {

    public static final String RIDER_NUMBER = "rider_number";
    public static final String FOCUS = "focus";

    @Inject
    private RiderManager riderManager;
    private int number = 99;

    Rider rider = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_rider_input);
        RoboGuice.getInjector(this).injectMembers(this);

        final EditText firstNameView = (EditText) findViewById(R.id.first_name);
        final EditText lastNameView = (EditText) findViewById(R.id.last_name);
        final EditText numberView = (EditText) findViewById(R.id.number);
        final Spinner nationalitySpinner = (Spinner) findViewById(R.id.country);
        final Spinner bibSpinner = (Spinner) findViewById(R.id.bib);
        final CheckBox genderButton = (CheckBox) findViewById(R.id.gender);
        numberView.setText(Integer.toString(number));
        final TextView errorText = (TextView) findViewById(R.id.error_text);
        final int riderNumber = getIntent().getIntExtra(RIDER_NUMBER, -1);
        final EditText sharingWithView = (EditText) findViewById(R.id.sharing_with);
        final EditText bikeView = (EditText) findViewById(R.id.bike);
        final EditText riderTextView = (EditText) findViewById(R.id.rider_text);

        final ImageView riderImage = (ImageView) findViewById(R.id.rider_image);
        final ImageView bikeImage = (ImageView) findViewById(R.id.bike_image);

        Button b2016 = (Button)findViewById(R.id.up_2016);
        b2016.setVisibility(View.GONE);
        b2016.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                riderManager.updateTo2016(rider);
            }
        });

        Button buttonEU = (Button)findViewById(R.id.button_eu);
        buttonEU.setVisibility(View.GONE);
        buttonEU.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                riderManager.updateToEU(rider);
            }
        });

        ArrayAdapter<CharSequence> countrySpinAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item);
        countrySpinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        for (Country item : Country.values()) {
            countrySpinAdapter.add(item.name());
        }

        nationalitySpinner.setAdapter(countrySpinAdapter);

        ArrayAdapter<CharSequence> bibSpinAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item);
        countrySpinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        for (Bib item : Bib.values()) {
            bibSpinAdapter.add(item.name());
        }

        bibSpinner.setAdapter(bibSpinAdapter);

        if (riderNumber >= 0) {

            try {
                rider = riderManager.getRider(riderNumber);

                firstNameView.setText(rider.getFirstName());
                lastNameView.setText(rider.getLastName());
                numberView.setText(rider.getRiderNumberString());
                genderButton.setChecked(rider.getGender() == Gender.F);
                sharingWithView.setText(Integer.toString(rider.getSharing()));
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

                if (rider.getCountry() == null) {
                    rider.setCountry(Constants.country);
                }
                for (int i = 0; i < Country.values().length; i++) {
                    if (Country.values()[i] == rider.getNationality()) {
                        nationalitySpinner.setSelection(i);
                        break;
                    }
                }


                if (rider.getBib() == null) {
                    rider.setBib(Bib.Y);
                } else {
                    for (int i = 0; i < Bib.values().length; i++) {
                        if (Bib.values()[i] == rider.getBib()) {
                            bibSpinner.setSelection(i);
                            break;
                        }
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
                errorText.setText(e.getMessage());
            }

        } else {
            rider = new Rider();
        }

        nationalitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rider.setNationality(Country.values()[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bibSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rider.setBib(Bib.values()[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ((Button) findViewById(R.id.deleteButton)).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                riderManager.deleteRider(rider, new RidersCallback() {

                    @Override
                    public void onSuccess() {
                        finish();
                    }

                    @Override
                    public void onError() {
                        errorText.setText("Delete failed....");
                    }
                });
            }
        });


        ((Button) findViewById(R.id.okButton)).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                String firstName = firstNameView.getText().toString();
                String lastName = lastNameView.getText().toString();
                Gender gender = genderButton.isChecked() ? Gender.F : Gender.M;
                String numberString = numberView.getText().toString();
                String nationalityString = (String) nationalitySpinner.getSelectedItem();
                String bibString = (String) bibSpinner.getSelectedItem();
                String bike = bikeView.getText().toString();
                String riderText = riderTextView.getText().toString();

                String sharingText = sharingWithView.getText().toString();
                int sharing = 0;
                if (sharingText != null && sharingText.length() > 0) {
                    sharing = Integer.parseInt(sharingWithView.getText().toString());
                }
                Country nationality = Constants.country;
                if (nationalityString != null) {
                    nationality = Country.valueOf(nationalityString);
                }

                Bib bib = Bib.Y;
                if (bibString != null) {
                    bib = Bib.valueOf(bibString);
                }

                if (numberString != null && StringUtils.isNumeric(numberString)) {
                    number = Integer.parseInt(numberString);
                }

                rider.setFirstName(firstName);
                rider.setLastName(lastName);
                rider.setGender(gender);
                rider.setRiderNumber(number);
                rider.setNationality(nationality);
                rider.setCountry(Constants.country);
                rider.setSeason(Constants.season);
                rider.setSharing(sharing);
                rider.setBike(bike);
                rider.setText(riderText);
                rider.setBib(bib);

                if (firstName != null && lastName != null) {

                    try {
                        riderManager.createOrUpdate(rider, new UpdateRiderCallback() {

                            @Override
                            public void onSuccess() {
                                finish();
                            }

                            @Override
                            public void onError(String error) {
                                errorText.setText(error);
                            }
                        });
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }
}
