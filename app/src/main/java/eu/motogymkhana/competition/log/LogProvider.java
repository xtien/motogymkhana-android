/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.log;

import eu.motogymkhana.competition.log.impl.MyLogImpl;

/**
 * Created by christine on 4-7-16.
 */
public class LogProvider {

    static MyLog logger;

    public static void setLogger(MyLog log) {
        logger = log;
    }

    public static MyLog getLogger() {

        if(logger == null){
            logger = new MyLogImpl();
        }
        return logger;
    }
}
