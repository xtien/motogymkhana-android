/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.notify;

import eu.motogymkhana.competition.adapter.ChangeListener;

/**
 * Created by christine on 28-6-16.
 */
public interface Notifier {

    void registerRiderResultListener(ChangeListener listener);

    void unRegisterRiderResultListener(ChangeListener listener);

    void notifyDataChanged();
}
