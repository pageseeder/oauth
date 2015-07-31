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

import javax.servlet.http.HttpServletRequest;

import org.pageseeder.oauth.server.OAuthAccessToken;
import org.pageseeder.oauth.server.OAuthListener;
import org.pageseeder.oauth.server.OAuthTemporaryToken;

/**
 * A simple 'No Operation Performed' implementation of the <code>OAuthListener</code> interface.
 *
 * @author Christophe Lauret
 * @version 28 October 2011
 */
public final class NOPListener implements OAuthListener {

  /**
   * Do nothing.
   * {@inheritDoc}
   */
  @Override
  public void initiate(OAuthTemporaryToken token, HttpServletRequest req) {
  }

  /**
   * Do nothing.
   * {@inheritDoc}
   */
  @Override
  public void authorize(OAuthTemporaryToken token, HttpServletRequest req) {
  }

  /**
   * Do nothing.
   * {@inheritDoc}
   */
  @Override
  public void token(OAuthTemporaryToken temporary, OAuthAccessToken token, HttpServletRequest req) {
  }

  /**
   * Do nothing.
   * {@inheritDoc}
   */
  @Override
  public void filter(OAuthAccessToken token, HttpServletRequest req) {
  }

}
