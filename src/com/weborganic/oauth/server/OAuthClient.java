package com.weborganic.oauth.server;

import com.weborganic.oauth.OAuthCredentials;

/**
 * Encapsulates information about an OAuth client.
 * 
 * <p>All methods below must return a value.
 * 
 * @author Christophe Lauret
 * @version 23 July 2011
 */
public interface OAuthClient {

  /**
   * Returns a unique identifier for this client.
   * 
   * @return a unique identifier for this client.
   */
  String id();

  /**
   * Returns the credentials for this client.
   * 
   * @return the Client credentials.
   */
  OAuthCredentials getCredentials();

  /**
   * Returns the callback URL this client should use.
   * 
   * <p>This method should return <code>"oob"</code> if using the "Out Of Band" configuration.
   * 
   * @return the callback URL used by this client or <code>"oob"</code>.
   */
  String getCallbackURL();

}