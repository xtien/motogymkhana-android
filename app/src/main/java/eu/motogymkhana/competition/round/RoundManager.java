/**
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.round;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import eu.motogymkhana.competition.api.ResponseHandler;
import eu.motogymkhana.competition.model.Round;

/**
 * Created by christine on 26-5-15.
 * RoundManager handles all round related actions in the app, including oploading and downloading round data.
 */
public interface RoundManager {

    /**
     * get date of round "number"
     */
    Round getDate(int number) throws SQLException;

    /**
     * set current round to "date"
     */
    void setDate(long date) throws SQLException;

    /**
     * get the next round, given the current round.
     */
    Round getNextRound() throws ParseException, SQLException;

    /**
     * upload all rounds for current season and country to server
     */
    void uploadRounds(ResponseHandler responseHandler) throws SQLException;

    /**
     * get rounds locally
     */
    List<Round> getRounds() throws SQLException;

    /**
     * get rounds from server and store them
     */
    void loadRoundsFromServer();

    /**
     * get the round that is current.
     */
    Round getCurrentRound() throws SQLException;

    /**
     * save rounds locally
     */
    void save(List<Round> rounds) throws SQLException;

    /**
     * get a round for a given date
     */
    Round getRoundForDate(long date) throws SQLException;
}
