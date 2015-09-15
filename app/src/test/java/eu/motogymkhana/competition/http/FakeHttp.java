package eu.motogymkhana.competition.http;

public interface FakeHttp {

	FakeHttpResult get(String url);

	void put(String urlString, int i, String string, String string2);

}
