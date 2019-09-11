package de.poc.ejb;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import org.jboss.ejb3.annotation.SecurityDomain;

@SecurityDomain("EjbSecurityDomain")
@Stateless(name = "GreetingsEJB")
@Remote({GreetingsRemote.class})
@RolesAllowed("POC-User")
public class GreetingsEJB implements GreetingsRemote {
  //******************************************************************
  // attributes
  //******************************************************************

  //******************************************************************
  // lifecycle
  //****************************************************************

  //******************************************************************
  // bean properties
  //******************************************************************

  //******************************************************************
  // public interface
  //******************************************************************
  @Override
  public String greet(String pName) {
    return "Hello " + pName + "! Nice to meet you here. Have a nice day =)!";
  }

  //******************************************************************
  // implementation
  //******************************************************************

}
