/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.api.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by christine on 19-1-16.
 */
public class ExecutorFactory {

    private static ExecutorService es;

    public static ExecutorService get() {

        if (es == null) {
            es = Executors.newCachedThreadPool();
        }

        return es;
    }
}
