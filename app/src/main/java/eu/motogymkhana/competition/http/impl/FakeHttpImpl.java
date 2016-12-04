/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.http.impl;

import java.util.HashMap;

import javax.inject.Singleton;

import eu.motogymkhana.competition.http.FakeHttp;
import eu.motogymkhana.competition.http.FakeHttpResult;

@Singleton
public class FakeHttpImpl implements FakeHttp {

	private HashMap<String, FakeHttpResult> results = new HashMap<String, FakeHttpResult>();

	@Override
	public FakeHttpResult get(String url) {

		return results.get(url);
	}

	@Override
	public void put(String urlString, int statusCode, String reason, String content) {

		results.put(urlString, new FakeHttpResult(statusCode, reason, content));
	}

}
