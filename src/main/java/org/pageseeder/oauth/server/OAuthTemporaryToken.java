package org.pageseeder.oauth.server;

import org.pageseeder.oauth.OAuthCredentials;

/**
 * A temporary single use OAuth token.
 * 
 * <p>Tokens are immutable objects.
 * 
 * @author Christophe Lauret
 * @version 21 July 2011
 */
public final class OAuthTemporaryToken implements OAuthToken {

  /**
   * Associated credentials.
   */
  private final OAuthCredentials _credentials;

  /**
   * When this token expires
   */
  private final long _expires;

  /**
   * The client for which this token was granted.
   */
  private final OAuthClient _client;

  /**
   * The verifier string.
   */
  private final String _verifier;

  /**
   * The Callback URL associated with this token
   */
  private final String _callback;

  /**
   * State variable to indicate that this token has been used 
   */
  private boolean _used;

  /**
   * Creates a new OAuth token with the specified credentials.
   * 
   * @param credentials The token credentials.
   * @param client      The client for which this token is granted
   * @param verifier    The verifier for this token.
   * @param expires     When this token expires.
   * @param callback    The callback URL for verification.
   */
  public OAuthTemporaryToken(OAuthCredentials credentials, OAuthClient client, String verifier, long expires, String callback) {
    this._credentials = credentials;
    this._expires = expires;
    this._client = client;
    this._verifier = verifier;
    this._callback = callback;
    this._used = false;
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
   * Returns the verifier string for this token.
   * 
   * @return the verifier string for this token.
   */
  public String verifier() {
    return this._verifier;
  }

  /**
   * Indicates whether this token has already been used.
   * 
   * @return the verifier string for this token.
   */
  public boolean isUsed() {
    return this._used;
  }

  /**
   * Mark this token as already used.
   */
  public void marksAsUsed() {
    this._used = true;
  }

  /**
   * Returns the call back URL used to verify this token.
   * 
   * @return the call back URL for this temporary token.
   */
  public String callbackURL() {
    return this._callback;
  }

  /**
   * @return Always <code>null</code>.
   */
  @Override
  public String scope() {
    return null;
  }
}
