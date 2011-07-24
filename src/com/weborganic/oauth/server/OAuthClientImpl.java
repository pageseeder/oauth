package com.weborganic.oauth.server;

import com.weborganic.oauth.OAuthConstants;
import com.weborganic.oauth.OAuthCredentials;

/**
 * Encapsulates information about an OAuth client.
 * 
 * @author Christophe Lauret
 * @version 20 July 2011
 */
public class OAuthClientImpl implements OAuthClient {
  
  /**
   * The name of the OAuth client.
   */
  private final String _name;

  /**
   * The OAuth credentials for this client.
   */
  private OAuthCredentials _credentials;

  /**
   * The call back URL.
   */
  private String _callback;

  private String description;

  private String website;

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
   * 
   * @param credentials A new set of credentials for this client.
   */
  protected void setCredentials(OAuthCredentials credentials) {
    this._credentials = credentials;
  }

  /**
   * @param callback the callback to set
   */
  public void setCallbackURL(String callback) {
    this._callback = callback;
  }


}
