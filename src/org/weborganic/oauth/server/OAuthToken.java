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
  public abstract OAuthClient client();

  /**
   * @return the token credentials.
   */
  public abstract OAuthCredentials credentials();

  /**
   * Returns the date the OAuth token expires.
   * 
   * @return the date the token expires.
   */
  public abstract long expires();

  /**
   * Indicates whether the validity of this token has expired.
   * 
   * @return <code>true</code> if the token has expired.
   *         <code>false</code> otherwise.
   */
  public abstract boolean hasExpired();

}