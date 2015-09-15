package eu.motogymkhana.competition.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.inject.Inject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.R;
import eu.motogymkhana.competition.adapter.ChangeListener;
import eu.motogymkhana.competition.adapter.MyViewPagerAdapter;
import eu.motogymkhana.competition.context.ContextProvider;
import eu.motogymkhana.competition.fragment.RiderRegistrationFragment;
import eu.motogymkhana.competition.fragment.RiderTimeInputFragment;
import eu.motogymkhana.competition.fragment.RidersResultFragment;
import eu.motogymkhana.competition.fragment.SeasonTotalsFragment;
import eu.motogymkhana.competition.model.Round;
import eu.motogymkhana.competition.prefs.ChristinePreferences;
import eu.motogymkhana.competition.rider.RiderManager;
import eu.motogymkhana.competition.rider.RiderManagerProvider;
import eu.motogymkhana.competition.round.RoundManager;
import eu.motogymkhana.competition.round.RoundManagerProvider;
import roboguice.activity.RoboFragmentActivity;

public class MainActivity extends RoboFragmentActivity {

    private static final int NEW_RIDER = 101;
    private static final int ADMIN = 102;
    private static final int TEXT = 103;
    private static final int SETTINGS = 104;
    private static final int SELECT_DATE = 105;

    private static final String TAG = MainActivity.class.getSimpleName();

    @SuppressWarnings("unused")
    @Inject
    private ContextProvider contextProvider;

    private ViewPager viewPager;

    private Handler handler;

    @Inject
    private RiderManager riderManager;

    @Inject
    private RoundManager roundManager;

    @Inject
    private ChristinePreferences prefs;

    private TextView dateView;
    private TextView messageTextView;

    private Runnable loadRidersTask = new Runnable() {

        @Override
        public void run() {

            if (!prefs.isAdmin()) {
                riderManager.loadRidersFromServer();
                messageTextView.setText(riderManager.getMessageText());

                handler.postDelayed(loadRidersTask, Constants.refreshRate);
            }
        }
    };

    private Runnable loadDatesTask = new Runnable() {

        @Override
        public void run() {

            roundManager.loadRoundsFromServer();
        }
    };

    private ChangeListener dataChangedListener = new ChangeListener() {

        @Override
        public void notifyDataChanged() {

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    dateView.setText(Constants.dateFormat.format(roundManager.getDate()));
                }
            });
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {

            case ADMIN:
                if (prefs.isAdmin()) {
                    setFragments();
                    setDate();
                }
                break;

            case SELECT_DATE:
                dateView.setText(Constants.dateFormat.format(roundManager.getDate()));
                break;

            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {

            int versionCode =  getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;

            if(prefs.getVersionCode() != versionCode){
                prefs.setAdmin(false);
                prefs.setVersionCode(versionCode);
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            prefs.setAdmin(false);
            prefs.setPassword(null);
        }

        setContentView(R.layout.activity_main);

        RiderManagerProvider.setContext(this);
        RoundManagerProvider.setContext(this);

        Collection<Round> rounds = null;

        try {
            rounds = roundManager.getRounds();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (prefs.isAdmin()) {

                if (rounds == null || rounds.size() == 0) {
                    roundManager.loadDates();
                    roundManager.setDate(roundManager.getNextRound().getDate());
                }
           }

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        handler = new Handler();

        dateView = (TextView) findViewById(R.id.date);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(3);

        messageTextView = ((TextView) findViewById(R.id.message_text));

        setFragments();

        dateView.setText(Constants.dateFormat.format(roundManager.getDate()));

        if (!prefs.isAdmin()) {
            handler.post(loadRidersTask);
            handler.post(loadDatesTask);
        }

        if (prefs.startUpTimes() > 0) {

            handler.postDelayed(new Runnable() {

                @Override
                public void run() {

                    viewPager.setCurrentItem(1);

                    handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            viewPager.setCurrentItem(0);
                        }
                    }, 2000l);
                }
            }, 2000l);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        handler.postDelayed(loadRidersTask, Constants.refreshRate);

        if (riderManager != null) {
            riderManager.registerRiderResultListener(dataChangedListener);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacksAndMessages(null);
        if (riderManager != null) {
            riderManager.unRegisterRiderResultListener(dataChangedListener);
        }
    }

    private void setFragments() {
        List<Fragment> fragments = new ArrayList<Fragment>();

        if (prefs.isAdmin()) {
            fragments.add(new RiderRegistrationFragment());
        }

        fragments.add(new RiderTimeInputFragment());
        fragments.add(new RidersResultFragment());
        fragments.add(new SeasonTotalsFragment());

        viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager(), fragments));
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        boolean admin = prefs.isAdmin();
        Collection<Round> rounds = null;
        try {
            rounds = roundManager.getRounds();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        int numberOfRounds = rounds == null ? 0 : rounds.size();

        menu.findItem(R.id.load_file).setVisible(admin);
        menu.findItem(R.id.download_riders).setVisible(admin);
        menu.findItem(R.id.upload_file).setVisible(admin);
        menu.findItem(R.id.upload_dates).setVisible(admin);
        menu.findItem(R.id.new_rider).setVisible(admin);
        menu.findItem(R.id.date1).setVisible(numberOfRounds > 0);
        menu.findItem(R.id.start_numbers).setVisible(admin);
        menu.findItem(R.id.text).setVisible(admin);
        menu.findItem(R.id.settings).setVisible(prefs.isAdmin());
        menu.findItem(R.id.save_witty_file).setVisible(prefs.isAdmin());

        menu.findItem(R.id.admin).setVisible(!prefs.isAdmin());

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {

            case R.id.settings:
                startActivityForResult(new Intent(this, SettingsActivity.class), SETTINGS);
                return true;

            case R.id.date1:
                startActivityForResult(new Intent(this, SelectDateActivity.class), SELECT_DATE);
                return true;

            case R.id.start_numbers:
                try {
                    riderManager.generateStartNumbers();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return true;

            case R.id.new_rider:
                startActivityForResult(new Intent(this, RiderNewUpdateActivity.class), NEW_RIDER);
                return true;

            case R.id.load_file:
                try {
                    riderManager.loadRidersFile();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return true;

            case R.id.download_riders:
                try {
                    riderManager.downloadRiders();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;

            case R.id.upload_file:

                try {
                    riderManager.uploadRiders();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return true;

            case R.id.upload_dates:

                try {
                    roundManager.uploadRounds();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return true;

            case R.id.save_witty_file:
                riderManager.saveWittyFile();
                return true;

            case R.id.admin:

                startActivityForResult(new Intent(this, AdminActivity.class), ADMIN);
                return true;

            case R.id.text:

                startActivityForResult(new Intent(this, TextActivity.class), TEXT);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setDate() {

        try {
            setDate(roundManager.getNextRound().getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setDate(long l) throws SQLException {

        roundManager.setDate(l);
        dateView.setText(Constants.dateFormat.format(new Date(l)));

        riderManager.notifyDataChanged();

    }

    protected void copyFile(InputStream is, File dst) throws IOException {

        FileOutputStream fos = new FileOutputStream(dst);

        byte[] buffer = new byte[65536 * 2];
        int read;
        while ((read = is.read(buffer)) != -1) {
            fos.write(buffer, 0, read);
        }

        is.close();
        fos.flush();
        fos.close();

    }
}
