package org.pageseeder.oauth.base;

import javax.servlet.http.HttpServletRequest;

import org.pageseeder.oauth.server.OAuthListener;
import org.pageseeder.oauth.server.OAuthAccessToken;
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
