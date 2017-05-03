/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition;

/**
 * Created by christine on 4-12-16.
 */

public class Server {

    public static boolean USE_HTTPS = true;
    public static final String urlHttp = USE_HTTPS ? "https://" : "http://";
    public static final String hostName = "api.gymcomp.com";
    public static final int HTTP_PORT = USE_HTTPS ? 9005 : 8085;
}
