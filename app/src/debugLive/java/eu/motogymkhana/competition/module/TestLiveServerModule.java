/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.module;

import android.content.Context;

import eu.motogymkhana.competition.api.http.MyHttp;
import eu.motogymkhana.competition.api.http.impl.MyHttpImpl;
import toothpick.config.Module;

public class TestLiveServerModule extends Module {

	public TestLiveServerModule(Context context) {
		bind(MyHttp.class).to(MyHttpImpl.class);
	}
}
