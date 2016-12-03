/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.module;

import android.content.Context;

import javax.inject.Provider;

import eu.motogymkhana.competition.MotoGymkhanaApplication;

/**
 * Created by christine on 21-4-16.
 * CtxProvider was created to allow for selection of a different context in unit tests.
 */
public class CtxProvider implements Provider<Context> {

    @Override
    public Context get() {
        return MotoGymkhanaApplication.getContext();
    }
}
