package com.weborganic.oauth.server;

import com.weborganic.oauth.OAuthCredentials;


/**
 * An OAuth token that provides access to the API.
 * 
 * <p>Tokens are immutable objects.
 * 
 * @author Christophe Lauret
 * @version 20 July 2011
 */
public final class OAuthAccessToken implements OAuthToken {

  
  private final OAuthCredentials _credentials;

  private final long _expires;

  private final OAuthClientImpl _client;

  /**
   * Creates a new OAuth token with the specified credentials.
   * 
   * @param credentials The token credentials.
   * @param expires     When this token expires.
   * @param client      The client for which this token is granted
   */
  public OAuthAccessToken(OAuthCredentials credentials, long expires, OAuthClientImpl client) {
    this._credentials = credentials;
    this._expires = expires;
    this._client = client;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public OAuthClientImpl client() {
    return this._client;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public OAuthCredentials credentials() {
    return this._credentials;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long expires() {
    return this._expires;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasExpired() {
    return System.currentTimeMillis() - this._expires > 0;
  }


}
