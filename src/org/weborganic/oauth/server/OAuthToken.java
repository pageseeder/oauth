package org.weborganic.oauth.server;

import org.weborganic.oauth.OAuthCredentials;

/**
 * A temporary single use OAuth token.
 * 
 * <p>Tokens are immutable objects.
 * 
 * @author Christophe Lauret
 * @version 20 July 2011
 */
public interface OAuthToken {

  /**
   * @return the OAuth client associated with the token
   */
  OAuthClient client();

  /**
   * @return the token credentials.
   */
  OAuthCredentials credentials();

  /**
   * Returns the date the OAuth token expires.
   * 
   * @return the date the token expires.
   */
  long expires();

  /**
   * Indicates whether the validity of this token has expired.
   * 
   * @return <code>true</code> if the token has expired.
   *         <code>false</code> otherwise.
   */
  boolean hasExpired();

  /**
   * Returns the scope of the token.
   * 
   * <p>This value may be <code>null</code> and its meaning is application specific.
   * 
   * @return the scope of this token (may be <code>null</code>).
   */
  String scope();

}