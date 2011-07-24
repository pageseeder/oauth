package com.weborganic.oauth.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import com.weborganic.oauth.OAuthException;
import com.weborganic.oauth.OAuthProblem;
import com.weborganic.oauth.util.URLs;


public final class OAuthErrorHandler {

  /**
   * Handles the specfied OAuthException.
   * 
   * @param res
   * @param ex
   * 
   * @throws IOException
   */
  public static void handle(HttpServletResponse res, OAuthException ex) throws IOException {
    OAuthProblem problem = ex.getProblem();
    int code = ex.getProblem().getHttpCode();
    if (!res.isCommitted()) {
      res.reset();
      res.setStatus(code);
      if (code == 401) { res.setHeader("WWW-Authenticate", "OAuth"); }

      // Write error
      printOAuth(res, ex);
      
    } else {
      res.sendError(problem.getHttpCode(), problem.name());
    }
  }

  /**
   * Print the error details as XML.
   * 
   * @param res The response.
   * @param ex  The exception.
   * 
   * @throws IOException If the printer misbehaves.
   */
  public static void printXML(HttpServletResponse res, OAuthException ex) throws IOException {
    OAuthProblem problem = ex.getProblem();
    int code = problem.getHttpCode();
    res.setCharacterEncoding("utf-8");
    res.setContentType("application/xml;charset=utf-8");
    // Write error
    PrintWriter out = res.getWriter();
    out.println("<?xml version=\"1.0\"/>");
    out.println("<response>");
    out.println("  <oauth_parameter name=\"oauth_error_code\">"+code+"</oauth_parameter>");
    out.println("  <oauth_parameter name=\"oauth_error_text\">"+(code == 401? "Unauthorized" : "Bad request")+"</oauth_parameter>");
    out.println("  <oauth_parameter name=\"oauth_problem\">"+problem+"</oauth_parameter>");
    out.println("</response>");
    out.println();
  }

  /**
   * Print the error details as a basic <code>"application/x-www-form-urlencoded"</code> response
   * following OAuth conventions.
   * 
   * @param res The response.
   * @param ex  The exception.
   * 
   * @throws IOException If the printer misbehaves.
   */
  public static void printOAuth(HttpServletResponse res, OAuthException ex) throws IOException {
    OAuthProblem problem = ex.getProblem();
    int code = problem.getHttpCode();
    res.setContentType("application/x-www-form-urlencoded");
    PrintWriter out = res.getWriter();
    out.print("oauth_error_code=" + code);
    out.print("&oauth_error_text="+ URLs.encode((code == 401? "Unauthorized" : "Bad request")));
    out.print("&oauth_problem="+URLs.encode(problem.toString()));
  }
}
