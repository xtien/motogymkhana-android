/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.round;

import java.util.Comparator;

import eu.motogymkhana.competition.model.Round;

/**
 * Created by christine on 7-7-16.
 */
public class RoundComparatorByDate implements java.util.Comparator<Round> {

    @Override
    public int compare(Round lhs, Round rhs) {
        return (lhs.getDate() - rhs.getDate()) < 0 ? -1 : 1;
    }
}
