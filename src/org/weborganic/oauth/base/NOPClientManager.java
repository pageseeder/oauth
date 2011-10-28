package org.weborganic.oauth.base;

import org.weborganic.oauth.server.ClientManager;
import org.weborganic.oauth.server.OAuthClient;

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
