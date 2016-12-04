package eu.motogymkhana.competition.http;

public class FakeHttpResult {

	private String content;
	private int statusCode;
	private String reasonPhrase;

	public FakeHttpResult(int statusCode, String reasonPhrase, String content) {
		this.content = content;
		this.statusCode = statusCode;
		this.reasonPhrase = reasonPhrase;
	}

	public String getContent() {
		return content;
	}

	public String getReasonPhrase() {
		return reasonPhrase;
	}

	public int getStatusCode() {
		return statusCode;
	}
}
