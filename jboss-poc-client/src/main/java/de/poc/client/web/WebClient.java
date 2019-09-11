package de.poc.client.web;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

import de.poc.jwt.JwtTokenDO;

public class WebClient {

  public String get(final String pHost, final Integer pPort, final String pResource, final JwtTokenDO pJwtToken) {
    String lResponse = null;
    try {
      HttpUriRequest lTokenUriRequest = null;
      if (null != pJwtToken) {
        lTokenUriRequest = RequestBuilder.get() // 
            .setUri("https://" + pHost + ":" + pPort + pResource) //
            .addHeader("Authorization", "Bearer " + pJwtToken.getToken()) //
            .addParameter("name", pJwtToken.getSub()) //
            .build();
      } else {
        lTokenUriRequest = RequestBuilder.get().setUri("https://" + pHost + ":" + pPort + pResource).build();
      }

      final HttpClient lClient = createHttpClient();
      final HttpResponse lTokenResponse = lClient.execute(lTokenUriRequest);

      lResponse = lTokenResponse.getStatusLine().toString() + " " + EntityUtils.toString(lTokenResponse.getEntity());
    } catch (Exception lE) {
      lResponse = lE.getMessage();
    }
    return lResponse;
  }

  private HttpClient createHttpClient() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
    final TrustStrategy lTrustALL = (cert, authType) -> true;
    final SSLContext lSSLContext = SSLContextBuilder.create().loadTrustMaterial(null, lTrustALL).build();
    final SSLConnectionSocketFactory lConnSF = new SSLConnectionSocketFactory(lSSLContext, NoopHostnameVerifier.INSTANCE);
    final Registry<ConnectionSocketFactory> lSocketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create().register("https", lConnSF).build();
    final BasicHttpClientConnectionManager lConnManager = new BasicHttpClientConnectionManager(lSocketFactoryRegistry);

    final HttpClientBuilder lHttpClientBuilder = HttpClients.custom().setSSLSocketFactory(lConnSF).setConnectionManager(lConnManager);
    return lHttpClientBuilder.build();
  }
}
