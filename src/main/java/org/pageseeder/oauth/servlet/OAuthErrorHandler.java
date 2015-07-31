/*
 * Copyright 2015 Allette Systems (Australia)
 * http://www.allette.com.au
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.pageseeder.oauth.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.pageseeder.oauth.OAuthException;
import org.pageseeder.oauth.OAuthProblem;
import org.pageseeder.oauth.util.URLs;


/**
 * A generic error handler for OAuth errors.
 *
 * @author Christophe Lauret
 * @version 21 December 2011
 */
public final class OAuthErrorHandler {

  /**
   * Handles the specified OAuthException.
   *
   * @param res The HTTP Response
   * @param ex  The error that needs handling
   *
   * @throws IOException If thrown while dealing with the Servlet response.
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
