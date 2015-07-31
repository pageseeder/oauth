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

import java.util.Collection;

/**
 * Classes implementing this interface should be able to create and manage tokens.
 *
 * <p>Applications using this API should provide their own implementation of a <code>TokenFactory</code>
 * as the default token factory mostly useful for testing purposes but will not scale.
 *
 * @author Christophe Lauret
 * @version 27 July 2011
 */
public interface TokenFactory {

  /**
   * Return the specified OAuth token instance using the token string.
   *
   * @param token the token string.
   * @return the corresponding OAuth token or <code>null</code>.
   */
  OAuthAccessToken get(String token);

  /**
   * Returns the access tokens.
   *
   * @param upTo The max number of tokens to return.
   * @return the access tokens.
   */
  Collection<OAuthAccessToken> listTokens(int upTo);

  /**
   * Returns the access tokens.
   *
   * @return the access tokens.
   */
  Collection<OAuthAccessToken> listTokens();

  /**
   * Creates new token credentials for the specified client.
   *
   * @param client The OAuth client for which this token is issued.
   * @return A new OAuth token.
   */
  OAuthAccessToken newToken(OAuthClient client);

  /**
   * Remove the specified token effectively revoking access for the client currently using the token.
   *
   * @return The token that was removed.
   */
  OAuthAccessToken revoke(String token);

  /**
   * Remove all the tokens which are stale.
   *
   * @return the number of tokens which were removed.
   */
  int clearStale();

}
