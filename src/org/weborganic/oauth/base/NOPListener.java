package org.weborganic.oauth.base;

import javax.servlet.http.HttpServletRequest;

import org.weborganic.oauth.server.OAuthListener;
import org.weborganic.oauth.server.OAuthAccessToken;
import org.weborganic.oauth.server.OAuthTemporaryToken;

/**
 * A simple 'No Operation Performed' implementation of the <code>Callbacks</code> interface.
 * 
 * @author Christophe Lauret
 * @version 26 October 2011
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
