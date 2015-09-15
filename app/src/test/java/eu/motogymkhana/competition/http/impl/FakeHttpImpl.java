package eu.motogymkhana.competition.http.impl;

import com.google.inject.Singleton;

import java.util.HashMap;

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
