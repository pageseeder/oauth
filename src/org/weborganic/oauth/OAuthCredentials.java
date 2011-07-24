package org.weborganic.oauth;

/**
 * Credentials use by OAuth entities.
 * 
 * <p>OAuth credentials consist of an key/identifier and a shared secret.
 * 
 * <p>This object is immutable.
 * 
 * @author Christophe Lauret
 * @version 21 July 2011
 */
public final class OAuthCredentials {

  /**
   * The identifier for this set of credentials.
   */
  private final String _identifier;

  /**
   * The shared secret for this set of credentials.
   */
  private final String _secret;

  /**
   * Create a new pair of OAuth credentials.
   * 
   * @param identifier The identifier for this set of credentials.
   * @param secret     The shared secret for this set of credentials.
   */
  public OAuthCredentials(String identifier, String secret) {
    this._identifier = identifier;
    this._secret = secret;
  }

  /**
   * @return The identifier for this set of credentials which may be the token key or client identifier.
   */
  public String identifier() {
    return this._identifier;
  }

  /**
   * @return The shared secret for this set of credentials.
   */
  public String secret() {
    return this._secret;
  }
}
