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
package org.pageseeder.oauth.base;

import java.util.Collection;
import java.util.Collections;

import org.pageseeder.oauth.server.OAuthAccessToken;
import org.pageseeder.oauth.server.OAuthClient;
import org.pageseeder.oauth.server.TokenFactory;

/**
 * A simple 'No Operation Performed' implementation of the <code>TokenFactory</code> interface.
 *
 * @author Christophe Lauret
 * @version 28 October 2011
 */
public final class NOPTokenFactory implements TokenFactory {

  /**
   * A singleton.
   */
  private static final NOPTokenFactory SINGLETON = new NOPTokenFactory();

  /**
   * Does nothing.
   *
   * @return Always 0.
   */
  @Override
  public int clearStale() {
    return 0;
  }

  /**
   * @return Always <code>null</code>.
   */
  @Override
  public OAuthAccessToken get(String token) {
    return null;
  }

  /**
   * @return Always an empty list.
   */
  @Override
  public Collection<OAuthAccessToken> listTokens(int upTo) {
    return Collections.emptyList();
  }

  /**
   * @return Always an empty list.
   */
  @Override
  public Collection<OAuthAccessToken> listTokens() {
    return Collections.emptyList();
  }

  /**
   * @return Always <code>null</code>.
   */
  @Override
  public OAuthAccessToken newToken(OAuthClient client) {
    return null;
  }

  /**
   * Does nothing.
   * @return Always <code>null</code>.
   */
  @Override
  public OAuthAccessToken revoke(String token) {
    return null;
  }


  /**
   * Returns the sole instance.
   * @return the singleton.
   */
  public static NOPTokenFactory singleton() {
    return SINGLETON;
  }
}
