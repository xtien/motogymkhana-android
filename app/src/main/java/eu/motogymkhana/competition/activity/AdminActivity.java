package eu.motogymkhana.competition.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.inject.Inject;

import java.sql.SQLException;

import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.R;
import eu.motogymkhana.competition.dao.CredentialDao;
import eu.motogymkhana.competition.model.Credential;
import eu.motogymkhana.competition.prefs.ChristinePreferences;
import roboguice.RoboGuice;

/**
 * Created by christine on 19-5-15.
 */
public class AdminActivity extends BaseActivity {

     private String noPw;
    private String pwShort;

    @Inject
    private ChristinePreferences prefs;

    @Inject
    private CredentialDao credentialDao;
    private Credential credential;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin);
        RoboGuice.getInjector(this).injectMembers(this);

        noPw = getString(R.string.no_password);
        pwShort = getString(R.string.password_too_short);


        final EditText pwView = (EditText) findViewById(R.id.password);
        final TextView errorView = (TextView) findViewById(R.id.error_text);

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        try {
            credential = credentialDao.get();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (credential == null) {
            credential = new Credential();
            credential.setCountry(Constants.country);
        }

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
                                    credential.setAdmin(true);
                                    credential.setPassword(pwView.getText().toString());
                                    credentialDao.store(credential);
                                    finish();
                                } else {
                                    errorView.setText(noPw);
                                }
                            } catch (Exception e) {
                                credential.setAdmin(false);
                                credential.setPassword(pwView.getText().toString());
                                try {
                                    credentialDao.store(credential);
                                } catch (SQLException e1) {
                                    e1.printStackTrace();
                                    showAlert(e);
                                }
                            }
                        }
                    }).execute();
                }
            }
        });
    }
}
