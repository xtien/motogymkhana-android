package eu.motogymkhana.competition.rider;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import eu.motogymkhana.competition.adapter.ChangeListener;
import eu.motogymkhana.competition.adapter.TotalsListAdapter;
import eu.motogymkhana.competition.api.impl.RidersCallback;
import eu.motogymkhana.competition.model.Rider;

public interface RiderManager {

    void getRiders(GetRidersCallback callback);

    void registerRiderResultListener(ChangeListener listener);

    void unRegisterRiderResultListener(ChangeListener listener);

    Rider getRiderByNumber(int riderNumber) throws SQLException;

    void update(Rider rider, UpdateRiderCallback callback);

    void update(Rider rider) throws SQLException, IOException;

    Rider store(Rider rider) throws SQLException;

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

    void setRegistered(Rider rider, boolean isChecked) throws SQLException;

    void generateStartNumbers() throws SQLException;

    void notifyDataChanged();
}
