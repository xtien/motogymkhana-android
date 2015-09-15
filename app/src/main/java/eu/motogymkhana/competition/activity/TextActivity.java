package eu.motogymkhana.competition.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.inject.Inject;

import eu.motogymkhana.competition.R;
import eu.motogymkhana.competition.rider.RiderManager;
import roboguice.activity.RoboActivity;

/**
 * Created by christine on 19-5-15.
 */
public class TextActivity extends RoboActivity {

    @Inject
    private RiderManager riderManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_text);

        final EditText textView = (EditText) findViewById(R.id.edit_text);

        Button okButton = (Button) findViewById(R.id.okButton);

        okButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                riderManager.sendText(textView.getText().toString());
                     finish();
            }
        });

    }
}
