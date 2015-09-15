package eu.motogymkhana.competition.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.inject.Inject;

import eu.motogymkhana.competition.R;
import eu.motogymkhana.competition.prefs.ChristinePreferences;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectResource;

/**
 * Created by christine on 19-5-15.
 */
public class SettingsActivity extends RoboActivity {

    String pw = "gsxr";
    @InjectResource(R.string.no_password)
    private String noPw;
    String pww = "1200";
    @Inject
    private ChristinePreferences prefs;
    String pwww = "GS";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        final EditText serverView = (EditText) findViewById(R.id.server);
        final EditText portView = (EditText) findViewById(R.id.port);
        final TextView errorView = (TextView) findViewById(R.id.error_text);

        serverView.setText(prefs.getServer());
        portView.setText(Integer.toString(prefs.getPort()));

        Button okButton = (Button) findViewById(R.id.okButton);

        okButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                prefs.setServer(serverView.getText().toString());
                prefs.setPort(Integer.parseInt(portView.getText().toString()));
                finish();
            }
        });

    }
}
