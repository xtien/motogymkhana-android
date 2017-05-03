/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.module;

import android.content.Context;

import eu.motogymkhana.competition.api.http.MyHttp;
import eu.motogymkhana.competition.http.FakeHttp;
import eu.motogymkhana.competition.http.impl.FakeHttpImpl;
import eu.motogymkhana.competition.http.impl.TestMyHttpImpl;
import eu.motogymkhana.competition.util.FileAssetManager;
import eu.motogymkhana.competition.util.FileAssetManagerImpl;
import toothpick.config.Module;

public class TestModule extends Module {

	public TestModule(Context context) {

		bind(MyHttp.class).to(TestMyHttpImpl.class);
		bind(FakeHttp.class).to(FakeHttpImpl.class);

		bind(FileAssetManager.class).to(FileAssetManagerImpl.class);
	}
}
