package org.weborganic.oauth.base;

import org.weborganic.oauth.OAuthConstants;
import org.weborganic.oauth.OAuthCredentials;
import org.weborganic.oauth.server.OAuthClient;

/**
 * Encapsulates information about an OAuth client.
 * 
 * @author Christophe Lauret
 * @version 25 October 2011
 */
public class OAuthClientImpl implements OAuthClient {

  /**
   * The name of the OAuth client.
   */
  private final String _name;

  /**
   * The OAuth credentials for this client (required).
   */
  private OAuthCredentials _credentials;

  /**
   * The call back URL (optional).
   */
  private String _callback;

  /**
   * The OAuth client description. 
   */
  private String description;

  /**
   * The OAuth client website. 
   */
  private String website;

  /**
   * Indicates whether the client requires authorization from the user;
   * or whether access is implicitly granted to the client.
   */
  private boolean _privileged;

  /**
   * Creates a new client.
   * 
   * @param name        The name of this client.
   * @param credentials The set of credentials for this client.
   */
  protected OAuthClientImpl(String name, OAuthCredentials credentials) {
    this._name = name;
    this._credentials = credentials;
    this._callback = OAuthConstants.OUT_OF_BAND;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String id() {
    return this._name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public OAuthCredentials getCredentials() {
    return this._credentials;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getCallbackURL() {
    return this._callback;
  }

  /**
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * @param description the description to set
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * @return the website
   */
  public String getWebsite() {
    return website;
  }

  /**
   * @param website the website to set
   */
  public void setWebsite(String website) {
    this.website = website;
  }

  /**
   * Set the OAuth credentials for this client.
   * 
   * @param credentials A new set of credentials for this client.
   */
  protected void setCredentials(OAuthCredentials credentials) {
    this._credentials = credentials;
  }

  /**
   * @param callback the callback URL to set.
   */
  public void setCallbackURL(String callback) {
    this._callback = callback;
  }

  /**
   * Sets whether access can be implicitly granted to the client or whether the resource owner
   * must explicitly grant access to the client.
   * 
   * @param privileged <code>true</code> if the client requires user authorization;
   *                   <code>false</code> otherwise.
   */
  protected void setPrivileged(boolean privileged) {
    this._privileged = privileged;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isPrivileged() {
    return this._privileged;
  }

}
