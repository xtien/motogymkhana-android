/**
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

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

/**
 * created by Christine
 * RiderManager handles all actions regarding riders, including uploading and downloading.
 */
public interface RiderManager {

    /**
     * Get riders from server for current country and season
     */
    void getRiders(GetRidersCallback callback);

    /**
     * register a listener for changes in rider data. This is also used for other changes, I should probably
     * rename it to "changeListener"
     */
    void registerRiderResultListener(ChangeListener listener);

    /**
     * unregister listener for rider changes
     */
    void unRegisterRiderResultListener(ChangeListener listener);

    /**
     * get a rider, given their rider number.
     */
    Rider getRiderByNumber(int riderNumber) throws SQLException;

    /**
     * update a rider on the server asynchronously
     */
    void update(Rider rider, UpdateRiderCallback callback);

    /**
     * copy a rider to the EU data. This was used for copying all NL riders to the EU database section.
     */
    @Deprecated
    void updateToEU(Rider rider);

    /**
     * update rider locally and on server synchronously
     */
    void update(Rider rider) throws SQLException, IOException;

    /**
     * update rider times sync
     */
    void update(Times times) throws SQLException, IOException;

    /**
     * store rider object locally
     */
    Rider store(Rider rider, Country country, int season) throws SQLException;

    /**
     * get totals for current country and season
     */
    void getTotals(TotalsListAdapter totalsListAdapter) throws SQLException, IOException;

    /**
     * create a new rider or update an existing rider on server
     */
    void createOrUpdate(Rider rider, UpdateRiderCallback callback) throws SQLException;

    /**
     * loads a file with today's riders. This is used to load a file that was created by a Witty Time computer.
     */
    void loadRidersFile() throws IOException, SQLException;

    /**
     * upload riders to server (this season and country)
     */
    void uploadRiders() throws IOException, SQLException;

    /**
     * download riders from server fro this season and country
     */
    void downloadRiders() throws IOException;

    /**
     * get a rider by their rider number, for current season and country.
     */
    Rider getRider(int riderNumber) throws SQLException;

    /**
     * load all riders from server.
     */
    void loadRidersFromServer();

    /**
     * delete a rider locally and remotely
     */
    void deleteRider(Rider rider, RidersCallback callback);

    /**
     * Send a text to the server to be displayed in the app heading and in the web page heading. This should
     * be refactored, it's not a rider thing.
     */
    void sendText(String text);

    /**
     * set message text in ridermanager. This is an implementation thing, should probably go.
     */
    void setMessageText(String text);

    /**
     * save a riders list in a format that the Witty time computer can read. This is typically used for exporting
     * a riders file after the startnumbers have been determined in the app. The Witty computer then contains a list
     * of riders in the right order with the right start numbers.
     */
    void saveWittyFile();

    /**
     * get all riders for date, locally
     */
    List<Rider> getRiders(long date);

    /**
     * get all riders registered for current round, locally
     */
    void getRegisteredRiders(GetRidersCallback callback);

    /**
     * set a rider as registered for a date. The Times object contains the rider and the date
     * isChecked: rider is registered/unregisterd
     */
    void setRegistered(Times times, boolean isChecked) throws SQLException;

    /**
     * Attribute random start numbers to riders for current round
     */
    void generateStartNumbers() throws SQLException;

    /**
     * notify rider manager that data has changed so it can notify its registered listeners. This is an implementation
     * thing, should probably refactored.
     */
    void notifyDataChanged();

    /**
     * copy a rider from the 2015 list to the 2016 list.
     */
    @Deprecated
    void updateTo2016(Rider rider);
}
