package eu.motogymkhana.competition.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.inject.Inject;

import eu.motogymkhana.competition.R;
import eu.motogymkhana.competition.prefs.ChristinePreferences;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectResource;

/**
 * Created by christine on 19-5-15.
 */
public class AdminActivity extends RoboActivity {

    @InjectResource(R.string.no_password)
    private String noPw;

    @InjectResource(R.string.password_too_short)
    private String pwShort;

    @Inject
    private ChristinePreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin);

        final EditText pwView = (EditText) findViewById(R.id.password);
        final TextView errorView = (TextView) findViewById(R.id.error_text);

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        Button okButton = (Button) findViewById(R.id.okButton);

        okButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (pwView.getText().toString().length() < 6) {
                    errorView.setText(pwShort);

                } else {

                    progressBar.setVisibility(View.VISIBLE);

                    new CheckPasswordTask(AdminActivity.this, pwView.getText().toString(), new MyPasswordCallback() {

                        @Override
                        public void onResult(boolean check) {

                            try {
                                progressBar.setVisibility(View.GONE);

                                if (check) {
                                    prefs.setAdmin(true);
                                    prefs.setPassword(pwView.getText().toString());
                                    finish();
                                } else {
                                    errorView.setText(noPw);
                                }
                            } catch (Exception e) {
                                prefs.setAdmin(false);
                                prefs.setPassword(null);
                            }
                        }
                    }).execute();
                }
            }
        });

    }
}
