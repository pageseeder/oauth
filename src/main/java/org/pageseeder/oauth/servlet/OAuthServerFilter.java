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

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.pageseeder.oauth.OAuthException;
import org.pageseeder.oauth.OAuthParameter;
import org.pageseeder.oauth.OAuthProblem;
import org.pageseeder.oauth.OAuthRequest;
import org.pageseeder.oauth.server.OAuthAccessToken;
import org.pageseeder.oauth.server.OAuthClient;
import org.pageseeder.oauth.server.OAuthConfig;
import org.pageseeder.oauth.signature.OAuthSignatures;
import org.pageseeder.oauth.signature.OAuthSigner;
import org.pageseeder.oauth.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Filters requests and check that the user has access to the underlying resource.
 *
 * <p>If the request is a valid OAuth request, that is the token credentials are valid, the filter
 * will invoke the rest of the chain.
 *
 * <p>Otherwise, an error will be returned corresponding to the specified {@link OAuthProblem}.
 *
 * @author Christophe Lauret
 * @version 28 October 2011
 */
public final class OAuthServerFilter implements Filter {

  /**
   * Logger.
   */
  private final static Logger LOGGER = LoggerFactory.getLogger(OAuthServerFilter.class);

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
  @Override
  public void init(FilterConfig config) throws ServletException {
  }

  /**
   * Do nothing.
   *
   * {@inheritDoc}
   */
  @Override
  public void destroy() {
  }

  /**
   * Does the filtering.
   *
   * {@inheritDoc}
   */
  @Override
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
    HttpSession session = req.getSession();
    boolean bypass = req.getAttribute(BYPASS_SESSION_ATTRIBUTE) != null
                  || session != null && session.getAttribute(BYPASS_SESSION_ATTRIBUTE) != null;

    if (!bypass) {

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

    // Grab the OAuth configuration
    OAuthConfig configuration = OAuthConfig.getInstance();

    // Grab the message
    OAuthRequest message = OAuthRequest.parse(req);

    // Check that all the OAuth parameters required for temporary credentials are specified.
    message.checkRequired(OAuthParameter.RESOURCE_CREDENTIALS_REQUIRED);

    // Identify the client using the consumer Key
    String key = message.getOAuthParameter(OAuthParameter.oauth_consumer_key);
    OAuthClient client = configuration.manager().getByKey(key);
    if (client == null) throw new OAuthException(OAuthProblem.consumer_key_unknown);
    LOGGER.debug("Identified client as {}", client.id());

    // Get parameters
    String method = message.getOAuthParameter(OAuthParameter.oauth_signature_method);
    String signature = message.getOAuthParameter(OAuthParameter.oauth_signature);

    // Check the token credentials
    String token = message.getOAuthParameter(OAuthParameter.oauth_token);
    OAuthAccessToken access = configuration.factory().get(token);
    if (access == null)
      throw new OAuthException(OAuthProblem.token_rejected);
    if (access.hasExpired())
      throw new OAuthException(OAuthProblem.token_expired);
    // TODO Token used and revoked...

    LOGGER.debug("Token {} is valid", token);

    // TODO Handle Refresh tokens

    // Verify signature
    String baseString = message.toSignatureBaseString();
    // TODO no need to create a new instance every time...
    OAuthSigner signer = OAuthSignatures.newSigner(method);
    String signatureCheck = signer.getSignature(baseString, client.getCredentials().secret(), access.credentials().secret());
    if (!Strings.equals(signature, signatureCheck)) {
      LOGGER.debug("Signatures do not match: expected {} but got {}", signatureCheck, signature);
      throw new OAuthException(OAuthProblem.signature_invalid);
    }

    LOGGER.debug("OAuth filter OK for {}", token);
    // Invoke OAuth listener
    configuration.listener().filter(access, req);

  }

}
