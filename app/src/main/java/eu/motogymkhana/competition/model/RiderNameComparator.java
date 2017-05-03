/*
 * Copyright (c) 2015 - 2017, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.model;

import java.util.Comparator;
import java.util.jar.Pack200;

/**
 * Created by christine on 19-5-15.
 */
public class RiderNameComparator implements Comparator<Rider> {

    private final boolean sortFirstName;

    public RiderNameComparator() {
        sortFirstName = false;
    }

    public RiderNameComparator(boolean firstName) {
        sortFirstName = true;
    }

    @Override
    public int compare(Rider rider1, Rider rider2) {

        if (sortFirstName) {
            return rider1.getFirstName().compareTo(rider2.getFirstName());
        } else {
            return rider1.getLastName().compareTo(rider2.getLastName());
        }
    }
}
