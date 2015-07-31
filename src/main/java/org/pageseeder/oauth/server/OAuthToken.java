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
package org.pageseeder.oauth.server;

import org.pageseeder.oauth.OAuthCredentials;

/**
 * A temporary single use OAuth token.
 *
 * <p>Tokens are immutable objects.
 *
 * @author Christophe Lauret
 * @version 20 July 2011
 */
public interface OAuthToken {

  /**
   * @return the OAuth client associated with the token
   */
  OAuthClient client();

  /**
   * @return the token credentials.
   */
  OAuthCredentials credentials();

  /**
   * Returns the date the OAuth token expires.
   *
   * @return the date the token expires.
   */
  long expires();

  /**
   * Indicates whether the validity of this token has expired.
   *
   * @return <code>true</code> if the token has expired.
   *         <code>false</code> otherwise.
   */
  boolean hasExpired();

  /**
   * Returns the scope of the token.
   *
   * <p>This value may be <code>null</code> and its meaning is application specific.
   *
   * @return the scope of this token (may be <code>null</code>).
   */
  String scope();

}