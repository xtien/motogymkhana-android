package eu.motogymkhana.competition.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import eu.motogymkhana.competition.fragment.MyAlert;

/**
 * Created by christine on 7-2-16.
 */
public class BaseActivity extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setTitle("");
     }

    protected void showAlert(final Exception e) {
        e.printStackTrace();
        showAlert(e.getClass().getSimpleName() + " " + (e.getMessage() != null ? e.getMessage() : ""));
    }

    protected void showAlert(final String text) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                MyAlert alert = new MyAlert(BaseActivity.this);
                alert.setText(text);
                alert.show();
            }
        });
    }
}
