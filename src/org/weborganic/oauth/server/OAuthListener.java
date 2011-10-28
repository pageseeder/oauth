package org.weborganic.oauth.server;

import javax.servlet.http.HttpServletRequest;

/**
 * An interface to provide callbacks for each OAuth end point and when an OAuth request is made.
 * 
 * @author Christophe Lauret
 * @version 28 October 2011
 */
public interface OAuthListener {

  /**
   * Method invoked after the OAuth Initiate end point was passed successfully. 
   * 
   * @param token The temporary token created by the end-point.
   * @param req   The HTTP Servlet request.
   */
  void initiate(OAuthTemporaryToken token, HttpServletRequest req);

  /**
   * Method invoked after the OAuth Authorize end point was passed successfully. 
   * 
   * @param token The temporary token passed for authorization.
   * @param req   The HTTP Servlet request.
   */
  void authorize(OAuthTemporaryToken token, HttpServletRequest req);

  /**
   * Method invoked after the OAuth Token end point was passed successfully. 
   * 
   * @param temporary The temporary token (now used).
   * @param token     The new token.
   * @param req       The HTTP Servlet request.
   */
  void token(OAuthTemporaryToken temporary, OAuthAccessToken token, HttpServletRequest req);

  /**
   * Method invoked after a successful OAuth request is made.
   * 
   * <p>This method is called before the filter chain is invoked. 
   * 
   * @param token The token passed for authorization.
   * @param req   The HTTP Servlet request.
   */
  void filter(OAuthAccessToken token, HttpServletRequest req);

}
