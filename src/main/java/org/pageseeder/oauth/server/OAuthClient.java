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
 * Encapsulates information about an OAuth client.
 *
 * <p>All methods below must return a value.
 *
 * @author Christophe Lauret
 * @version 23 July 2011
 */
public interface OAuthClient {

  /**
   * Returns a unique identifier for this client.
   *
   * @return a unique identifier for this client.
   */
  String id();

  /**
   * Returns the credentials for this client.
   *
   * @return the Client credentials.
   */
  OAuthCredentials getCredentials();

  /**
   * Returns the callback URL this client should use.
   *
   * <p>This method should return <code>"oob"</code> if using the "Out Of Band" configuration.
   *
   * @return the callback URL used by this client or <code>"oob"</code>.
   */
  String getCallbackURL();

  /**
   * Indicates whether access is implicitly granted to the client or not.
   *
   * <p>A <i>privileged</i> client does not require authorization from the resource owner. The
   * presence of the resource owner is sufficient.
   *
   * @return <code>true</code> if the client requires user authorization;
   *         <code>false</code> otherwise.
   */
  boolean isPrivileged();

}
