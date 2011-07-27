/*
 * Copyright (c) 2011 weborganic systems pty. ltd.
 */
package org.weborganic.oauth.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.weborganic.oauth.OAuthException;
import org.weborganic.oauth.OAuthParameter;
import org.weborganic.oauth.OAuthProblem;
import org.weborganic.oauth.OAuthRequest;
import org.weborganic.oauth.server.OAuthClient;
import org.weborganic.oauth.server.OAuthClients;
import org.weborganic.oauth.server.OAuthToken;
import org.weborganic.oauth.server.OAuthTokens;
import org.weborganic.oauth.signature.OAuthSignatures;
import org.weborganic.oauth.signature.OAuthSigner;
import org.weborganic.oauth.util.Strings;


/**
 * Filters request and check that the user has access to the underlying resource.
 * 
 * @author Christophe Lauret
 * @version 0.6.2 - 8 April 2011
 * @since 0.6.2
 */
public final class OAuthServerFilter implements Filter {

  /**
   * When this attribute is specified, there is no need to use OAuth.
   * 
   * <p>Use this when the client has already been authenticated by other means.
   */
  public static final String BYPASS_SESSION_ATTRIBUTE = "com.weborganic.oauth.servlet.bypass"; 
  
  /**
   * Do nothing.
   * 
   * {@inheritDoc}
   */
  public void init(FilterConfig config) throws ServletException {
  }

  /**
   * Do nothing.
   * 
   * {@inheritDoc}
   */
  public void destroy() {
  }

  /**
   * Does the filtering.
   * 
   * {@inheritDoc}
   */
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) 
     throws IOException, ServletException {
    // Use HTTP specific requests.
    doHttpFilter((HttpServletRequest)req, (HttpServletResponse)res, chain);
  }

  /**
   * Does the filtering.
   * 
   * @param req   the HTTP servlet request
   * @param res   the HTTP servlet response
   * @param chain The filter chain
   * 
   * @throws IOException      If thrown by any of the underlying filters or servlets.
   * @throws ServletException If thrown by any of the underlying filters or servlets.
   */
  private void doHttpFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) 
     throws IOException, ServletException {

    // Check whether we need to filter the request with OAuth
    HttpSession session = req.getSession(true);
    Object bypass = session.getAttribute(BYPASS_SESSION_ATTRIBUTE);

    if (bypass == null) {

      // Let's check the OAuth request
      try {
        checkOAuthRequest(req, res);
      } catch (OAuthException ex) {
        // Generic error handling following OAuth Problem extension
        OAuthProblem problem = ex.getProblem();
        res.sendError(problem.getHttpCode(), problem.name());
        return;
      }
    }

    // We made it here, we can go on
    chain.doFilter(req, res);
  }

  // ==============================================================================================

  /**
   * Checks that the request has access to the underlying resources.
   * 
   * @param req The HTTP servlet request
   * @param res The HTTP servlet response.
   * 
   * @throws OAuthException Should any OAuth related problem occur.
   * @throws IOException Should an error occur while writing the output stream.
   */
  private static final void checkOAuthRequest(HttpServletRequest req, HttpServletResponse res) throws OAuthException, IOException {

    // Grab the message
    OAuthRequest message = OAuthRequest.parse(req);

    // Check that all the OAuth parameters required for temporary credentials are specified.
    message.checkRequired(OAuthParameter.RESOURCE_CREDENTIALS_REQUIRED);

    // Identify the client using the consumer Key
    String key = message.getOAuthParameter(OAuthParameter.oauth_consumer_key);
    OAuthClient client = OAuthClients.getByKey(key);
    if (client == null) throw new OAuthException(OAuthProblem.consumer_key_unknown);

    // Get parameters
    String method = message.getOAuthParameter(OAuthParameter.oauth_signature_method);
    String signature = message.getOAuthParameter(OAuthParameter.oauth_signature);

    // Check the token credentials
    String token = message.getOAuthParameter(OAuthParameter.oauth_token);
    OAuthToken access = OAuthTokens.get(token);
    if (access == null)
      throw new OAuthException(OAuthProblem.token_rejected);
    if (access.hasExpired())
      throw new OAuthException(OAuthProblem.token_expired);
    // TODO Token used and revoked...

    // TODO Handle Refresh tokens

    // Verify signature
    String baseString = message.toSignatureBaseString();
    // TODO no need to create a new instance every time...
    OAuthSigner signer = OAuthSignatures.newSigner(method);
    String signatureCheck = signer.getSignature(baseString, client.getCredentials().secret(), access.credentials().secret());
    if (!Strings.equals(signature, signatureCheck)) {
      throw new OAuthException(OAuthProblem.signature_invalid);
    }

  }

}
