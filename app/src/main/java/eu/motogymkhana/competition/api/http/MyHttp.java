package eu.motogymkhana.competition.api.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public interface MyHttp {

	public HttpResultWrapper getStringFromUrl(String url) throws  IOException;

	public HttpResultWrapper postStringFromUrl(String url, String input)
			throws UnsupportedEncodingException,  IOException;
}
