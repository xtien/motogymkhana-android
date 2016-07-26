/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * created by Christine
 * adapter for the viewpager in MainActivity
 */
public class MyViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragments;

    public MyViewPagerAdapter(FragmentManager fragmentManager, List<Fragment> fragments) {
        super(fragmentManager);
        this.fragments = fragments;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    public void push(Fragment fragment) {
        fragments.add(fragment);
    }

    public void pop() {

        fragments.remove(fragments.size() - 1);
        notifyDataSetChanged();
    }
}
