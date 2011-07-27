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
  public abstract OAuthClientImpl client();

  /**
   * @return the token
   */
  public abstract OAuthCredentials credentials();

  /**
   * @return the expires
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