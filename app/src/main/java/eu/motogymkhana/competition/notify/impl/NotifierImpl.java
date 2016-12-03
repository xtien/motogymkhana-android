/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.notify.impl;

import javax.inject.Singleton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import eu.motogymkhana.competition.adapter.ChangeListener;
import eu.motogymkhana.competition.notify.Notifier;

/**
 * Created by christine on 28-6-16.
 */
@Singleton
public class NotifierImpl implements Notifier {

    final private List<ChangeListener> riderResultListeners = new CopyOnWriteArrayList<>();

    @Override
    public void registerRiderResultListener(ChangeListener listener) {
        riderResultListeners.add(listener);
    }

    @Override
    public void unRegisterRiderResultListener(ChangeListener listener) {

        if (riderResultListeners.contains(listener)) {
            riderResultListeners.remove(listener);
        }
    }

    @Override
    public void notifyDataChanged() {

        for (ChangeListener listener : riderResultListeners) {
            listener.notifyDataChanged();
        }
    }
}
