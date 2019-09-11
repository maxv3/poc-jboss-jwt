package de.poc.client.ejb;

import java.security.Provider;
import java.util.Properties;
import java.util.concurrent.Callable;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.wildfly.security.WildFlyElytronProvider;
import org.wildfly.security.auth.client.AuthenticationConfiguration;
import org.wildfly.security.auth.client.AuthenticationContext;
import org.wildfly.security.auth.client.MatchRule;
import org.wildfly.security.credential.BearerTokenCredential;
import org.wildfly.security.sasl.SaslMechanismSelector;

import de.poc.ejb.GreetingsRemote;
import de.poc.jwt.JwtTokenDO;

public class EjbClient {
  //******************************************************************
  // attributes
  //******************************************************************
  private final String EJB_POC_INFO = "ejb:jboss-poc-ear-1.0-SNAPSHOT/jboss-poc-ejb/GreetingsEJB!de.poc.ejb.GreetingsRemote";

  //******************************************************************
  // lifecycle
  //******************************************************************

  //******************************************************************
  // implementation
  //******************************************************************
  /**
   * 
   * @param pToken the jwt token which will be used to authenticate against the elytron subsystem
   * @param pHost the host 
   * @param pPort the port
   * @return the result of the ejb as a string, or the exception in case of failure
   */
  public String remoteEjbLookupWithJwtToken(JwtTokenDO pJwtToken, String pHost, Integer pPort) {
    final AuthenticationConfiguration lAuthConf = AuthenticationConfiguration.empty() //
        .useProviders(() -> new Provider[] {new WildFlyElytronProvider()}) //
        .setSaslMechanismSelector(SaslMechanismSelector.NONE.addMechanism("OAUTHBEARER")) //
        .useBearerTokenCredential(new BearerTokenCredential(pJwtToken.getToken())) //        
        .useHost(pHost) //
        .usePort(pPort);

    final AuthenticationContext lAuthContext = AuthenticationContext.empty().with(MatchRule.ALL, lAuthConf);
    String lRemoteResult = null;
    try {
      final String lUserName = (pJwtToken != null) ? pJwtToken.getSub() : "none";
      lRemoteResult = lAuthContext.runCallable(remoteEJBCall(pHost, pPort, lUserName));
    } catch (Exception lE) {
      lRemoteResult = ">>> Error lookupEJB: " + lE.getMessage();
    }
    return lRemoteResult;
  }

  /**
   * 
   * @param pToken the jwt token which will be used to authenticate against the elytron subsystem
   * @param pHost the host 
   * @param pPort the port
   * @return the result of the ejb as a string, or the exception in case of failure
   */
  public String remoteEjbLookupWithoutJwtToken(String pHost, Integer pPort) {
    final AuthenticationConfiguration lAuthConf = AuthenticationConfiguration.empty() //
        .useProviders(() -> new Provider[] {new WildFlyElytronProvider()}) //    
        .useHost(pHost) //
        .usePort(pPort);

    final AuthenticationContext lAuthContext = AuthenticationContext.empty().with(MatchRule.ALL, lAuthConf);
    String lRemoteResult = null;
    try {
      lRemoteResult = lAuthContext.runCallable(remoteEJBCall(pHost, pPort, "none"));
    } catch (Exception lE) {
      lRemoteResult = ">>> Error lookupEJB: " + lE.getMessage();
    }
    return lRemoteResult;
  }

  /**
   * 
   * @param pHost the target host
   * @param pPort the target port
   * @return 
   */
  private Callable<String> remoteEJBCall(String pHost, Integer pPort, String pName) {
    final Callable<String> lCallEJB = () -> {
      String lResult = null;
      try {
        final Properties lJndiProperties = new Properties();
        lJndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
        lJndiProperties.put(Context.PROVIDER_URL, "remote+https://" + pHost + ":" + pPort);

        final Context lContext = new InitialContext(lJndiProperties);
        final GreetingsRemote lGreetingsRemote = ((GreetingsRemote) lContext.lookup(EJB_POC_INFO));
        lResult = lGreetingsRemote.greet(pName);

      } catch (Exception lE) {
        lResult = ">>> Error lookupEJB: " + lE.getMessage();
      }
      return lResult;
    };
    return lCallEJB;
  }

}
