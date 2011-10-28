package org.weborganic.oauth.base;

import java.util.Collection;
import java.util.Collections;

import org.weborganic.oauth.server.OAuthAccessToken;
import org.weborganic.oauth.server.OAuthClient;
import org.weborganic.oauth.server.TokenFactory;

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
