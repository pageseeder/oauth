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
