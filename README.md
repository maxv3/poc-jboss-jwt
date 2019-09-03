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
