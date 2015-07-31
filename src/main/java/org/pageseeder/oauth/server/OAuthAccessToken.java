package org.pageseeder.oauth.server;

import org.pageseeder.oauth.OAuthCredentials;


/**
 * An OAuth token that provides access to the API.
 * 
 * <p>Tokens are immutable objects.
 * 
 * @author Christophe Lauret
 * @version 25 October 2011
 */
public final class OAuthAccessToken implements OAuthToken {

  
  private final OAuthCredentials _credentials;

  private final long _expires;

  private final OAuthClient _client;

  private final String _scope;

  /**
   * Creates a new OAuth token with the specified credentials.
   * 
   * @param credentials The token credentials.
   * @param expires     When this token expires.
   * @param client      The client for which this token is granted
   */
  public OAuthAccessToken(OAuthCredentials credentials, long expires, OAuthClient client) {
    this._credentials = credentials;
    this._expires = expires;
    this._client = client;
    this._scope = null;
  }

  /**
   * Creates a new OAuth token with the specified credentials.
   * 
   * @param credentials The token credentials.
   * @param expires     When this token expires.
   * @param client      The client for which this token is granted
   * @param scope       The scope of this token.
   */
  public OAuthAccessToken(OAuthCredentials credentials, long expires, OAuthClient client, String scope) {
    this._credentials = credentials;
    this._expires = expires;
    this._client = client;
    this._scope = scope;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public OAuthClient client() {
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

  /**
   * {@inheritDoc}
   */
  @Override
  public String scope() {
    return this._scope;
  }
}
