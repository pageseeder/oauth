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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pageseeder.oauth.OAuthException;
import org.pageseeder.oauth.OAuthParameter;
import org.pageseeder.oauth.OAuthProblem;
import org.pageseeder.oauth.OAuthRequest;
import org.pageseeder.oauth.server.OAuthClient;
import org.pageseeder.oauth.server.OAuthConfig;
import org.pageseeder.oauth.server.OAuthTemporaryToken;
import org.pageseeder.oauth.server.OAuthTokens;
import org.pageseeder.oauth.signature.OAuthSignatures;
import org.pageseeder.oauth.signature.OAuthSigner;
import org.pageseeder.oauth.util.Strings;
import org.pageseeder.oauth.util.URLs;


/**
 * A servlet providing an OAuth 1.0 endpoint to initiate the OAuth authentication flow.
 *
 * <p>This servlet SHOULD be mapped to an unprotected publicly available URI so that external
 * applications can access it.
 *
 * <p>See example Web descriptor configuration:
 * <pre>{@code
 *
 * <servlet>
 *   <servlet-name>OAuthInitiate</servlet-name>
 *   <servlet-class>com.weborganic.oauth.servlet.OAuthInitiateServlet</servlet-class>
 * </servlet>
 *
 * <servlet-mapping>
 *   <servlet-name>OAuthInitiate</servlet-name>
 *   <url-pattern>/initiate</url-pattern>
 * </servlet-mapping>
 *
 * }</pre>
 *
 * @author Christophe Lauret
 * @version 28 October 2011
 */
public final class OAuthInitiateServlet extends HttpServlet {

  /**
   * Serial version ID as per required by the Serializable interface.
   */
  private static final long serialVersionUID = -6882761292067386616L;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    res.setHeader("Allow", "POST");
    res.sendError(405, "Only POST can be used to request temporary OAuth credentials");
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    try {

      // Let's provide some temporary credentials if we can
      doProvideTemporaryCredentials(req, res);

    } catch (OAuthException ex) {
      // Generic error handling following OAuth Problem extension
      OAuthErrorHandler.handle(res, ex);
      return;
    }
  }

  /**
   * Provides temporary credentials to the client.
   *
   * <pre>
   * OAuth
   *   oauth_callback="http%3A%2F%2Fconsumer.localhost%3A8099%2Fprotected%2Ftest.html",
   *   oauth_signature="LWrPLpyz0YaDpoLXpC3lENI%2F%2BX8%3D",
   *   oauth_version="1.0",
   *   oauth_nonce="527588316",
   *   oauth_signature_method="HMAC-SHA1",
   *   oauth_consumer_key="bastille-key",
   *   oauth_timestamp="1311054073"
   * </pre>
   *
   * @param req The HTTP servlet request
   * @param res The HTTP servlet response.
   *
   * @throws OAuthException Should any OAuth related problem occur.
   * @throws IOException Should an error occur while writing the output stream.
   */
  private static final void doProvideTemporaryCredentials(HttpServletRequest req, HttpServletResponse res)
      throws OAuthException, IOException {

    // Grab the OAuth configuration
    OAuthConfig configuration = OAuthConfig.getInstance();

    // Grab the message
    OAuthRequest message = OAuthRequest.parse(req);

    // Check that all the OAuth parameters required for temporary credentials are specified.
    message.checkRequired(OAuthParameter.TEMPORARY_CREDENTIALS_REQUIRED);

    // Identify the client using the consumer Key
    String key = message.getOAuthParameter(OAuthParameter.oauth_consumer_key);
    OAuthClient client = configuration.manager().getByKey(key);
    if (client == null) throw new OAuthException(OAuthProblem.consumer_key_unknown);

    // Get parameters
    String method = message.getOAuthParameter(OAuthParameter.oauth_signature_method);
    String signature = message.getOAuthParameter(OAuthParameter.oauth_signature);

    // Verify signature
    String baseString = message.toSignatureBaseString();

    // TODO no need to create a new instance every time...
    OAuthSigner signer = OAuthSignatures.newSigner(method);
    String signatureCheck = signer.getSignature(baseString, client.getCredentials().secret(), null);

    if (!Strings.equals(signature, signatureCheck)) throw new OAuthException(OAuthProblem.signature_invalid);

    // TODO Handle Nonce and Timestamp to prevent replay attacks

    // Generate a new temporary token for the client
    String callback = message.getOAuthParameter(OAuthParameter.oauth_callback);
    if (!URLs.isValidCallback(callback))
      throw new OAuthException(OAuthProblem.parameter_rejected);
    OAuthTemporaryToken token = OAuthTokens.newTemporary(client, callback);

    // Invoke OAuth listener
    configuration.listener().initiate(token, req);

    // Return the results to the client
    res.setContentType("application/x-www-form-urlencoded");
    PrintWriter out = res.getWriter();
    out.print("oauth_token=" + token.credentials().identifier());
    out.print("&oauth_token_secret="+ token.credentials().secret());
    out.print("&oauth_callback_confirmed=true");
    out.println();
  }

}
