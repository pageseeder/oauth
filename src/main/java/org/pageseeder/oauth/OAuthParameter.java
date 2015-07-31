/*
 * Copyright 2015 Allette Systems (Australia)
 * http://www.allette.com.au
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
