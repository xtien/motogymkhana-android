/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.api;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by christine on 19-1-16.
 */
public class RequestParams {

    private List<Pair<String,String>> params = new ArrayList<Pair<String,String>>();

    public void put(String name, String value){
        params.add(new Pair<String,String>(name,value));
    }

    public List<Pair<String, String>> getParams() {
        return params;
    }

    public void add(Pair<String,String> pair) {
        params.add(pair);
    }

    public int size() {
        return params.size();
    }
}
