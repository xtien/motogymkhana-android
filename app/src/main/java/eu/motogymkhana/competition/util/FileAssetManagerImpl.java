/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.util;

import android.content.Context;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FileAssetManagerImpl implements FileAssetManager {

	@Inject
	protected Context context;

	@Override
	public String getFileContent(String name) throws IOException {

		return FileUtils.readFile(context, name);
	}

}
