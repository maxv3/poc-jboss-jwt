package de.poc;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import de.poc.client.ejb.EjbClient;
import de.poc.client.web.WebClient;
import de.poc.jwt.JwtTokenDO;
import de.poc.jwt.JwtTokenDecoder;

public class AppLauncher {
  //******************************************************************************
  // attributes
  //**********************************************************************************
  // Targetsystem configuration
  final private String HOST = "127.0.0.1";
  final private Integer PORT = 8443;

  //******************************************************************************
  // lifecycle
  //**********************************************************************************
  public static void main(String[] args) {

    // Copy the client truststore to your users home directory 
    System.setProperty("javax.net.ssl.trustStore", System.getProperty("user.home") + "/client.truststore");
    System.setProperty("javax.net.ssl.trustStorePassword", "changeit");

    // Add your JWT TOKEN here
    String JWT_TOKEN = "PUT_YOUR_JWT_TOKEN_HERE";
    if (args != null && args.length > 0) {
      JWT_TOKEN = args[0];
    }

    final JwtTokenDO lJwtToken = JwtTokenDecoder.getDecodedJwtToken(JWT_TOKEN);
    System.out.println(">>> -------------------------------------------------------- ");
    System.out.println(lJwtToken);
    System.out.println(">>> -------------------------------------------------------- ");

    Long lJwtTokenExpiresIn = ChronoUnit.SECONDS.between(LocalDateTime.now(), lJwtToken.getExp());
    if (lJwtTokenExpiresIn < 0) {
      System.out.println(">>> The token is no longer valid. Expired " + (lJwtTokenExpiresIn * -1) + " seconds ago.");
      lJwtTokenExpiresIn = 0L;
    } else {
      System.out.println(">>> The token is valid for " + lJwtTokenExpiresIn + " seconds");
      lJwtTokenExpiresIn = lJwtTokenExpiresIn * 1000;
    }
    System.out.println(">>> -------------------------------------------------------- ");

    final AppLauncher lLauncher = new AppLauncher();

    // WEB Test: Request against the undertow (Servlets)
    lLauncher.runWebClientTest(JWT_TOKEN);

    // EJB Test: Request against the ejb's
    lLauncher.runEjbClientTest(JWT_TOKEN);

    try {
      // Execute the calls after the jwt token expiry time reaches
      System.out.println(">>> ... Thread will sleep until the expiry time of the jwt token has been reached.");
      Thread.sleep(lJwtTokenExpiresIn);
    } catch (Exception lE) {
      lE.printStackTrace();
    }

    // WEB Test: Request against the undertow (Servlets)
    lLauncher.runWebClientTest(JWT_TOKEN);

    // EJB Test: Request against the ejb's
    lLauncher.runEjbClientTest(JWT_TOKEN);
  }

  //**********************************************************************************
  // implementation
  //**********************************************************************************
  /**
   * 
   * @param pJwtToken the jwt token which will be used to access the web resource
   */
  public void runWebClientTest(String pJwtToken) {
    final WebClient lWebClient = new WebClient();
    System.out.println(">>> SERVLET CALL");
    System.out.println(">>> --------------------------------------------------");

    final JwtTokenDO lJwtToken = JwtTokenDecoder.getDecodedJwtToken(pJwtToken);

    // Test 1: Request without a token. Expect: 401 Unauthorized
    final String lTest1Response = lWebClient.get(HOST, PORT, "/jboss-poc-web/greet", null);
    System.out.println(">>> " + LocalDateTime.now() + ": Request without a token: " + lTest1Response);

    // Test 2: Request with a valid token. Expect: 200 OK
    final String lTest2Response = lWebClient.get(HOST, PORT, "/jboss-poc-web/greet", lJwtToken);
    System.out.println(">>> " + LocalDateTime.now() + ": Request with a valid token: " + lTest2Response);

    // Test 3: Request with an invalid token. Expect: 403 FORBIDDEN
    lJwtToken.setExp(LocalDateTime.now().minus(10L, ChronoUnit.MINUTES));
    lJwtToken.setToken(JwtTokenDecoder.getEncodedJwtToken(lJwtToken));

    final String lTest3Response = lWebClient.get(HOST, PORT, "/jboss-poc-web/greet", lJwtToken);
    System.out.println(">>> " + LocalDateTime.now() + ": Request with an invalid token: " + lTest3Response);
    System.out.println(">>> -------------------------------------------------------- ");
  }

  /**
   * 
   * @param pJwtToken the jwt token which will be used to access the ejb resource
   */
  public void runEjbClientTest(String pJwtToken) {
    final EjbClient lEjbClient = new EjbClient();
    System.out.println(">>> EJB CALL");
    System.out.println(">>> --------------------------------------------------");

    final JwtTokenDO lJwtToken = JwtTokenDecoder.getDecodedJwtToken(pJwtToken);

    // Test 1: Request without a token. Expect: 401 Unauthorized
    final String lTest1Response = lEjbClient.remoteEjbLookupWithoutJwtToken(HOST, PORT);
    System.out.println(">>> " + LocalDateTime.now() + ": Remote EJB lookup without a token: " + lTest1Response);

    // Test 2: Request with a valid token. Expect: 200 OK
    final String lTest2Response = lEjbClient.remoteEjbLookupWithJwtToken(lJwtToken, HOST, PORT);
    System.out.println(">>> " + LocalDateTime.now() + ": Remote EJB lookup with a valid token: " + lTest2Response);

    // Test 3: Request with an invalid token. Expect: 403 FORBIDDEN
    lJwtToken.setExp(LocalDateTime.now().minus(10L, ChronoUnit.MINUTES));
    lJwtToken.setToken(JwtTokenDecoder.getEncodedJwtToken(lJwtToken));

    final String lTest3Response = lEjbClient.remoteEjbLookupWithJwtToken(lJwtToken, HOST, PORT);
    System.out.println(">>> " + LocalDateTime.now() + ": Remote EJB lookup with an invalid token: " + lTest3Response);
    System.out.println(">>> -------------------------------------------------------- ");
  }
}
