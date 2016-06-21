/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.robo;

import android.content.Context;

import com.google.inject.Provider;

import org.robolectric.RuntimeEnvironment;

/**
 * Created by christine on 21-4-16.
 */
public class TestCtxProvider implements Provider<Context> {

    @Override
    public Context get() {
        return RuntimeEnvironment.application;
    }
}
