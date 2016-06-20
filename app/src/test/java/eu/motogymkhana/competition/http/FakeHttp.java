/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.http;

/**
 * created by Christine
 * This class is used for storing api responses for every api call in a unit test. This allows
 * for offline testing of api code.
 */
public interface FakeHttp {

	/**
	 * get the response for this url
	 * @param url
	 * @return
     */
	FakeHttpResult get(String url);

	/**
	 * store a response for a given url. also stored the httpstatus
	 * @param urlString
	 * @param httpStatus
	 * @param reason reason phrase returned
     * @param content content returned
     */
	void put(String urlString, int httpStatus, String reason, String content);

}
