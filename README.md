# Proof of Concept: JBoss EJB SERVLETS JWT 

### This project is a small proof of concept which shows how to setup JBoss to use JWT Token validation for EJB, JMS and Servlet requests.

#### Project structure

- jboss-poc-client
  > Contains the implementation for the Elytron Client to request EJB, JMS and Servlet resources from the Server.

- jboss-poc-ear
  > Contains the EJB and WEB project. The .ear archive is used for the deployment on the server.

- jboss-poc-ejb
  > Contains a simple EJB implementation which returns some system information for valid requests.

- jboss-poc-web
  > Contains a simple Servlet implementation which returns some system information for valid requests.

pom.xml

#### Necessary adjustments before code execution

1. Run the configure-jboss.cli 
2. Copy the client.truststore to your home directory
3. Copy the server.jks into the jboss configuration folder (/standalone/configuration/)
4. Add your JWT certificate to the server.jks with the alias jwt-cert. This certificate will be used by jboss to validate the tokens.

#### Client execution
1. Put your JWT Token to the AppLauncher.java (If you are running the project from an IDE)
2. Run the fat jar: java -jar jboss-poc-client-1.0-SNAPSHOT-jar-with-dependencies.jar YOUR_JWT_TOKEN

#### Console Output

##### SERVLET CALL
###### A request without an jwt token will result to the http status 401 Unauthorized
  > Output: ...Request without a token: HTTP/1.1 401 Unauthorized ...

###### A request without an valid jwt token will result to the http status 200 OK
 > Output: ...Request with a valid token: HTTP/1.1 200 OK Hello maxim! Nice to meet you here. Have a nice day =)!

###### A request with an jwt token which has been expired will result to the http status 403 Forbidden
 > Output: ...Request with an invalid token: HTTP/1.1 403 Forbidden ...

##### EJB CALL
###### A request without an jwt token will fail
 > Output: ...Remote EJB lookup without a token: >>> Error lookupEJB: EJBCLIENT000409: No more destinations are available ...
 
###### A request with an valid token will be successfull 
 > Output: ...Remote EJB lookup with a valid token: Hello maixm! Nice to meet you here. Have a nice day =)!
 
###### A request with an jwt token which has been expired will fail
 > Outpit: ...Remote EJB lookup with an invalid token: >>> Error lookupEJB: EJBCLIENT000409: No more destinations are available...
