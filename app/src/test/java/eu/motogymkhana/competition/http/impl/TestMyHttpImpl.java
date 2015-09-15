package eu.motogymkhana.competition.http.impl;

import com.google.inject.Inject;

import java.io.IOException;

import eu.motogymkhana.competition.api.http.HttpResultWrapper;
import eu.motogymkhana.competition.api.http.MyHttp;
import eu.motogymkhana.competition.http.FakeHttp;
import eu.motogymkhana.competition.http.FakeHttpResult;
import eu.motogymkhana.competition.util.FileAssetManager;

public class TestMyHttpImpl implements MyHttp {

    @Inject
    private FakeHttp fakeHttp;

    @Inject
    private FileAssetManager assetManager;

    public TestMyHttpImpl() {

    }

    @Override
    public HttpResultWrapper getStringFromUrl(String url) throws
            IOException {

        FakeHttpResult result = fakeHttp.get(url);

        String content = assetManager.getFileContent(result.getContent());

        return new HttpResultWrapper(result.getStatusCode(), result.getReasonPhrase(), content);
    }

    @Override
    public HttpResultWrapper postStringFromUrl(String url, String input)
            throws  IOException {

        FakeHttpResult result = fakeHttp.get(url);

        String content = assetManager.getFileContent(result.getContent());

        return new HttpResultWrapper(result.getStatusCode(), result.getReasonPhrase(), content);
    }
}