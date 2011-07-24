/**
 * This package contains classes required for using OAuth in a Java Servlet environment.
 * 
 * <p>It provides a servlet for each of the OAuth protocol endpoints:
 * <ul>
 *   <li><code>OAuthInitiateServlet</code> to provide temporary credentials;</li>
 *   <li><code>OAuthAuthorizeServlet</code> to accept the resource owner authorization;</li>
 *   <li><code>OAuthTokenServlet</code> to provide token credentials;</li>
 * </ul>
 * 
 * <p>All three Servlets must be defined in the Web descriptor, see individual Servlets 
 * documentation for configuration details.
 * 
 * <p>The {@link com.weborganic.oauth.servlet.OAuthInitiateServlet} and 
 * {@link com.weborganic.oauth.servlet.OAuthTokenServlet} can be 
 * 
 * <p>Unless the API allows applications to register publicly, the
 * {@link com.weborganic.oauth.servlet.OAuthAuthorizeServlet} should be ALWAYS protected and 
 * required authentication by the resource owner.
 * 
 * <p>This package also provides a filter to provide access to resource via OAuth.
 */
package com.weborganic.oauth.servlet;
