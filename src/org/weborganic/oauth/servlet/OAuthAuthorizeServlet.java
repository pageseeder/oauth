package org.weborganic.oauth.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.weborganic.oauth.OAuthConstants;
import org.weborganic.oauth.OAuthException;
import org.weborganic.oauth.OAuthProblem;
import org.weborganic.oauth.server.OAuthConfig;
import org.weborganic.oauth.server.OAuthClient;
import org.weborganic.oauth.server.OAuthTemporaryToken;
import org.weborganic.oauth.server.OAuthTokens;
import org.weborganic.oauth.util.URLs;


/**
 * A servlet providing an OAuth 1.0 endpoint to authorize an external application.
 * 
 * <p>This servlet MUST be mapped to a protected URI so that only authenticated user can access it.
 * 
 * <p>This class assumes that authentication has already been performed, and simply process an 
 * authorization given by the resource owner.
 * 
 * <p>Typically, GET requests will display the Authorization form and POST requests will accept an 
 * application authorization. Both must be secure.
 * 
 * <p>The following initialization parameters must be specified:
 * <ul>
 *   <li><code>out-of-band-page</code> path to the page that will display the verifier when the 
 *   callback is <i>out-of-band</i> ("oob");</li>
 *   <li><code>authorize-form-page</code> path to the page that will allow the user to see the
 *   details of the application to authorize and accept or reject the authorization.
 *   </li>
 * </ul>
 * 
 * <p>See example Web descriptor configuration:
 * <pre>
 * {@code
 * <servlet>
 *   <servlet-name>OAuthAuthorize</servlet-name>
 *   <servlet-class>com.weborganic.oauth.servlet.OAuthAuthorizeServlet</servlet-class>
 *   <init-param>
 *    <param-name>out-of-band-page</param-name>
 *    <param-value>/oauth/oob.html</param-value>
 *  </init-param>
 *  <init-param>
 *    <param-name>authorize-form-page</param-name>
 *    <param-value>/oauth/authorize.html</param-value>
 *  </init-param>
 * </servlet>
 * 
 * <servlet-mapping>
 *   <servlet-name>OAuthAuthorize</servlet-name>
 *   <url-pattern>/authorize</url-pattern>
 * </servlet-mapping>
 * 
 * }</pre>
 * 
 * @author Christophe Lauret
 * @version 28 October 2011
 */
public final class OAuthAuthorizeServlet extends HttpServlet {

  /**
   * Serial version ID as per required by the Serializable interface.
   */
  private static final long serialVersionUID = 4249124644162116665L;

  /**
   * The path to the application authorization form where the user can authorize the application
   * once he has authenticated.
   */
  private String form = null;

  /**
   * The path to the Out-Of-Band page where the user can see the verifier code can to use.
   */
  private String oob = null;

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    this.form = config.getInitParameter("authorize-form-page");
    this.oob = config.getInitParameter("out-of-band-page");
  }

  @Override
  public void destroy() {
    super.destroy();
    this.oob = null;
    this.form = null;
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    try {

      // Grab the temporary credentials 
      OAuthTemporaryToken token = getTemporaryToken(req);

      // Check if client is privileged
      OAuthClient client = token.client();
      if (client.isPrivileged()) {

        // Automatically authorize the application
        authorize(req, res, token);

      } else {

        // Otherwise display the authorization form 
        if (this.form != null) {
          RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher(this.form);
          dispatcher.forward(req, res);
        } else {
          res.sendError(501, "No Authorization form available.");
        }        
      }

    } catch (OAuthException ex) {
      // Generic error handling following OAuth Problem extension
      OAuthErrorHandler.handle(res, ex);
    }

  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    try {

      // Grab the temporary credentials 
      OAuthTemporaryToken token = getTemporaryToken(req);

      // Complete the authorization process
      authorize(req, res, token);

    } catch (OAuthException ex) {
      // Generic error handling following OAuth Problem extension
      OAuthErrorHandler.handle(res, ex);
    }

  }

  // Private helpers
  // ----------------------------------------------------------------------------------------------

  /**
   * Returns the temporary token.
   * 
   * @param req   The servlet request.
   * 
   * @return token The temporary token.
   * 
   * @throws OAuthException If the token is rejected, has expired or was used. 
   */
  private OAuthTemporaryToken getTemporaryToken(HttpServletRequest req) throws OAuthException {
    // Return the temporary credentials if we can 
    String identifier = req.getParameter("oauth_token");
    OAuthTemporaryToken token = OAuthTokens.getTemporary(identifier);
    if (token == null)
      throw new OAuthException(OAuthProblem.token_rejected);
    if (token.hasExpired())
      throw new OAuthException(OAuthProblem.token_expired);
    if (token.isUsed())
      throw new OAuthException(OAuthProblem.token_used);
    return token;
  }

  /**
   * Complete the authorization process for the client application.
   * 
   * <p>This method will:
   * <ul>
   *   <li>either redirect the user to the Out-Of-Band page (which displays the verification code);</li>
   *   <li>or redirect the user to the callback URL.</li>
   * </ul>
   * 
   * @param req   The servlet request.
   * @param res   The servlet response.
   * @param token The temporary token.
   * 
   * @throws ServletException If thrown by the servlet 
   * @throws IOException      If thrown by the servlet
   */
  private void authorize(HttpServletRequest req, HttpServletResponse res, OAuthTemporaryToken token) throws ServletException, IOException {
    // Check URL callback
    String callback = token.callbackURL();

    // Invoke OAuth listener
    OAuthConfig configuration = OAuthConfig.getInstance();
    configuration.listener().authorize(token, req);

    // Redirect to the client or display Out-Of-Band page
    if (OAuthConstants.OUT_OF_BAND.equals(callback)) {
      if (this.oob == null) {
        // Requested out of band but hasn't been configured
        res.sendError(501, "out-of-band configuration is not supported");
      } else {
        // Forward to Out of Band page
        RequestDispatcher dispatcher = this.getServletConfig().getServletContext().getRequestDispatcher(this.oob);
        dispatcher.forward(req, res);
      }
    } else {
      res.sendRedirect(addOAuthInfo(callback, token));
    }
  }

  /**
   * Appends the OAuth token and verifier to the specified URL.
   * 
   * @param url   The URL
   * @param token The temporary token
   * 
   * @return The URL the user should be redirected to.
   */
  private String addOAuthInfo(String url, OAuthTemporaryToken token) {
    return url + (url.indexOf('?') >= 0? '&' : '?') +
    "oauth_token="+URLs.encode(token.credentials().identifier()) +
    "&oauth_verifier="+URLs.encode(token.verifier());
  }

}
