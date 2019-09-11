package de.poc.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/greet")
@ServletSecurity(@HttpConstraint(rolesAllowed = {"POC-User"}))
public class GreetingsServlet extends HttpServlet {
  //**********************************************************************************
  // attributes
  //**********************************************************************************
  private static final long serialVersionUID = 1L;

  //**********************************************************************************
  // lifecycle
  //**********************************************************************************

  //**********************************************************************************
  // implementation
  //**********************************************************************************
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.getWriter().write("Hello " + request.getParameter("name") + "! Nice to meet you here. Have a nice day =)!");
  }
}
