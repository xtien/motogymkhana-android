package eu.motogymkhana.competition.api.http;

import android.content.Context;

import com.google.inject.Inject;
import com.google.inject.Provider;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import eu.motogymkhana.competition.R;

/**
 * Created by christine on 10-6-15.
 */
public class SSLContextProvider implements Provider<SSLContext> {

    private SSLContext sslContext = null;

    @Inject
    private Context context;

    @Override
    public SSLContext get() {

        if (sslContext == null) {

            try {

                createSSLContext();

            } catch (KeyStoreException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }

        }

        return sslContext;
    }

    private void createSSLContext() throws KeyStoreException, IOException, NoSuchAlgorithmException,
            KeyManagementException {

        Security.addProvider(new BouncyCastleProvider());

        final KeyStore ks = KeyStore.getInstance("BKS");

        final InputStream in = context.getResources().openRawResource(R.raw.gossip);
        try {
            ks.load(in, context.getString(R.string.keystore_password).toCharArray());
        } catch (java.security.cert.CertificateException e) {
            e.printStackTrace();
        } finally {
            in.close();
        }

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(ks);

        sslContext = SSLContext.getInstance("SSL");

        sslContext.init(null, tmf.getTrustManagers(), null);
    }
}
