package org.weborganic.oauth.server;

import javax.servlet.http.HttpServletRequest;

/**
 * An interface to provide callbacks for each OAuth end point.
 * 
 * @author Christophe Lauret
 * @version 26 October 2011
 */
public interface Callbacks {

  /**
   * Method invoked when the OAuth Initiate end point was passed successfully. 
   * 
   * @param token The temporary token created by the end-point.
   * @param req   The HTTP Servlet request.
   */
  void initiate(OAuthTemporaryToken token, HttpServletRequest req);

  /**
   * Method invoked when the OAuth Authorize end point was passed successfully. 
   * 
   * @param token The temporary token passed for authorization.
   * @param req   The HTTP Servlet request.
   */
  void authorize(OAuthTemporaryToken token, HttpServletRequest req);

  /**
   * Method invoked when the OAuth Token end point was passed successfully. 
   * 
   * @param temporary The temporary token.
   * @param token     The token passed for authorization.
   * @param req       The HTTP Servlet request.
   */
  void token(OAuthTemporaryToken temporary, OAuthAccessToken token, HttpServletRequest req);

  /**
   * Method invoked when the OAuth filter is passed successfully. 
   * 
   * @param token The token passed for authorization.
   * @param req   The HTTP Servlet request.
   */
  void filter(OAuthAccessToken token, HttpServletRequest req);

}
