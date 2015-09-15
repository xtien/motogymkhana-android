package eu.motogymkhana.competition.api.http.impl;

import android.util.Log;

import com.google.inject.Inject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.api.http.HttpResultWrapper;
import eu.motogymkhana.competition.api.http.MyHttp;
import eu.motogymkhana.competition.api.http.NullHostNameVerifier;

/**
 * Created by christine on 9-6-15.
 */
public class MyHttpImpl implements MyHttp {

    private static final String TAG = MyHttpImpl.class.getSimpleName();

    private static int readTimeout = 10000;
    private static int connectTimeout = 15000;

    @Inject
    private SSLContext sslContext;

    @Override
    public HttpResultWrapper getStringFromUrl(String urlString) throws IOException {

        URL url = new URL(urlString);

        HttpResultWrapper result = null;
        String string = "";

        if (Constants.USE_HTTPS) {

            HttpsURLConnection.setDefaultHostnameVerifier(new NullHostNameVerifier());

            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();

            try {

                urlConnection.setConnectTimeout(5000);
                urlConnection.setSSLSocketFactory(sslContext.getSocketFactory());

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(in), 512);

                String line;
                while ((line = br.readLine()) != null) {
                    string = string + line;
                }

                in.close();

                int httpResult = urlConnection.getResponseCode();
                if (httpResult != HttpURLConnection.HTTP_OK) {
                    Log.e(TAG, urlConnection.getResponseMessage());
                }

                result = new HttpResultWrapper(httpResult, urlConnection.getResponseMessage(), string);

            } finally {
                urlConnection.disconnect();
            }
        } else {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            try {

                urlConnection.setConnectTimeout(5000);

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(in), 512);

                String line;
                while ((line = br.readLine()) != null) {
                    string = string + line;
                }

                in.close();

                int httpResult = urlConnection.getResponseCode();
                if (httpResult != HttpURLConnection.HTTP_OK) {
                    Log.e(TAG, urlConnection.getResponseMessage());
                }

                result = new HttpResultWrapper(httpResult, urlConnection.getResponseMessage(), string);

            } finally {
                urlConnection.disconnect();
            }
        }

        return result;
    }

    @Override
    public HttpResultWrapper postStringFromUrl(String urlString, String input) throws UnsupportedEncodingException,
            IOException {

        URL url = new URL(urlString);
        InputStream in;

        URLConnection urlConnection = null;

        HttpResultWrapper result = null;
        String string = "";
        int httpResult = 0;
        String responseMessage = "";

        urlConnection = url.openConnection();

        try {

            if (Constants.USE_HTTPS) {

                HttpsURLConnection.setDefaultHostnameVerifier(new NullHostNameVerifier());

                setPost((HttpsURLConnection) urlConnection);
                ((HttpsURLConnection) urlConnection).setSSLSocketFactory(sslContext.getSocketFactory());

                urlConnection.connect();

                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());

                wr.write(input);
                wr.flush();

                wr.close();
                int HttpResult = ((HttpsURLConnection) urlConnection).getResponseCode();
                if (HttpResult != HttpURLConnection.HTTP_OK) {
                    Log.e(TAG, ((HttpsURLConnection) urlConnection).getResponseMessage());
                }

                httpResult = ((HttpsURLConnection) urlConnection).getResponseCode();
                responseMessage = ((HttpsURLConnection) urlConnection).getResponseMessage();
                if (httpResult != HttpURLConnection.HTTP_OK) {
                    Log.e(TAG, ((HttpsURLConnection) urlConnection).getResponseMessage());
                }

                in = new BufferedInputStream(urlConnection.getInputStream());

            } else {

                setPost((HttpURLConnection) urlConnection);
                urlConnection.connect();

                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());

                wr.write(input);
                wr.flush();

                wr.close();
                int HttpResult = ((HttpURLConnection) urlConnection).getResponseCode();
                if (HttpResult != HttpURLConnection.HTTP_OK) {
                    Log.e(TAG, ((HttpURLConnection) urlConnection).getResponseMessage());
                }

                httpResult = ((HttpURLConnection) urlConnection).getResponseCode();
                responseMessage = ((HttpURLConnection) urlConnection).getResponseMessage();
                if (httpResult != HttpURLConnection.HTTP_OK) {
                    Log.e(TAG, ((HttpURLConnection) urlConnection).getResponseMessage());
                }

                in = new BufferedInputStream(urlConnection.getInputStream());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(in), 512);

            String line;
            while ((line = br.readLine()) != null) {
                string = string + line;
            }

            result = new HttpResultWrapper(httpResult, responseMessage, string);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            if (Constants.USE_HTTPS) {
                ((HttpsURLConnection) urlConnection).disconnect();
            } else {
                ((HttpURLConnection) urlConnection).disconnect();
            }
        }

        return result;
    }

    private void setPost(HttpURLConnection urlConnection) throws ProtocolException {

        urlConnection.setReadTimeout(readTimeout);
        urlConnection.setConnectTimeout(connectTimeout);
        urlConnection.setRequestMethod("POST");
        urlConnection.setDoOutput(true);
        urlConnection.setUseCaches(false);
        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.setRequestProperty("Accept", "application/json");
    }
}
