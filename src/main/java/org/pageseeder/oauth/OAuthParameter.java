package org.pageseeder.oauth;

import java.util.EnumSet;
import java.util.Set;


/**
 * An enumeration of OAuth parameters used by OAuth 1.0a.
 * 
 * @author  Christophe Lauret
 * @version 20 July 2011
 */
public enum OAuthParameter {

  oauth_callback,

  oauth_signature,

  oauth_version,

  oauth_nonce,

  oauth_signature_method,

  oauth_token,
  
  oauth_consumer_key,

  oauth_verifier,

  oauth_timestamp;

  /**
   * Set of OAuth parameters required to request temporary credentials.
   */
  public static final Set<OAuthParameter> TEMPORARY_CREDENTIALS_REQUIRED = EnumSet.of(oauth_consumer_key, oauth_signature_method, oauth_signature, oauth_timestamp, oauth_nonce, oauth_callback);

  /**
   * Set of OAuth parameters required to request token credentials.
   */
  public static final Set<OAuthParameter> TOKEN_CREDENTIALS_REQUIRED = EnumSet.of(oauth_consumer_key, oauth_token, oauth_signature_method, oauth_signature, oauth_timestamp, oauth_nonce, oauth_verifier);

  /**
   * Set of OAuth parameters required to request token credentials.
   */
  public static final Set<OAuthParameter> RESOURCE_CREDENTIALS_REQUIRED = EnumSet.of(oauth_consumer_key, oauth_token, oauth_signature_method, oauth_signature, oauth_timestamp, oauth_nonce);

}
