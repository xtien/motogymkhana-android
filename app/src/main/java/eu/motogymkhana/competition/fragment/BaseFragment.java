package eu.motogymkhana.competition.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import eu.motogymkhana.competition.dialog.MyAlert;

/**
 * Created by christine on 6-2-16.
 */
public abstract class BaseFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void showAlert(final Exception e) {
        e.printStackTrace();
        showAlert(e.getClass().getSimpleName() + " " + (e.getMessage() != null ? e.getMessage() : ""));
    }


    protected void showAlert(final String text) {

        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                MyAlert alert = new MyAlert(getContext());
                alert.setText(text);
                alert.show();
            }
        });
    }

    public abstract void onBackPressed();
}
