package eu.motogymkhana.competition.rider;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import eu.motogymkhana.competition.adapter.ChangeListener;
import eu.motogymkhana.competition.adapter.TotalsListAdapter;
import eu.motogymkhana.competition.api.impl.RidersCallback;
import eu.motogymkhana.competition.model.Country;
import eu.motogymkhana.competition.model.Rider;
import eu.motogymkhana.competition.model.Times;

public interface RiderManager {

    void getRiders(GetRidersCallback callback);

    void registerRiderResultListener(ChangeListener listener);

    void unRegisterRiderResultListener(ChangeListener listener);

    Rider getRiderByNumber(int riderNumber) throws SQLException;

    void update(Rider rider, UpdateRiderCallback callback);

    void updateToEU(Rider rider);

    void update(Rider rider) throws SQLException, IOException;

    void update(Times times) throws SQLException, IOException;

    Rider store(Rider rider, Country country, int season) throws SQLException;

    void getTotals(TotalsListAdapter totalsListAdapter) throws SQLException;

    void createOrUpdate(Rider rider, UpdateRiderCallback callback) throws SQLException;

    void loadRidersFile() throws IOException, SQLException;

    void uploadRiders() throws IOException, SQLException;

    void downloadRiders() throws IOException;

    Rider getRider(int riderNumber) throws SQLException;

    void loadRidersFromServer();

    void deleteRider(Rider rider, RidersCallback callback);

    void sendText(String text);

    String getMessageText();

    void setMessageText(String text);

    void saveWittyFile();

    List<Rider> getRiders(long date);

    void getRegisteredRiders(GetRidersCallback callback);

    void setRegistered(Times times, boolean isChecked) throws SQLException;

    void generateStartNumbers() throws SQLException;

    void notifyDataChanged();

    void updateTo2016(Rider rider);
}
