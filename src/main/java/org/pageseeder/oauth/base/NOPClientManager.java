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

import org.pageseeder.oauth.server.ClientManager;
import org.pageseeder.oauth.server.OAuthClient;

/**
 * A simple 'No Operation Performed' implementation of the <code>ClientManager</code> interface.
 *
 * @author Christophe Lauret
 * @version 28 October 2011
 */
public final class NOPClientManager implements ClientManager {

  /**
   * A singleton.
   */
  private static final NOPClientManager SINGLETON = new NOPClientManager();

  /**
   * @return Always <code>null</code>.
   */
  @Override
  public OAuthClient getByKey(String identifier) {
    return null;
  }

  /**
   * Returns the sole instance.
   * @return the singleton.
   */
  public static NOPClientManager singleton() {
    return SINGLETON;
  }

}
