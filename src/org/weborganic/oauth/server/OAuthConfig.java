package org.weborganic.oauth.server;

import org.weborganic.oauth.base.NOPClientManager;
import org.weborganic.oauth.base.NOPListener;
import org.weborganic.oauth.base.NOPTokenFactory;


/**
 * Centralise the OAuth server configuration in one class.
 * 
 * <p>The servlets and filters use this class to manage tokens and clients as well as invoke
 * the call back methods at each end point. 
 * 
 * <p>Because the configuration may be initialised at different times, implementations should always
 * request the configuration using the {@link #getInstance()} method.
 * 
 * @author Christophe Lauret
 * @version 28 October 2011
 */
public final class OAuthConfig {

  /**
   * The configuration to use.
   */
  private static OAuthConfig config = null;

  /**
   * The listener implementation to use.
   */
  private final OAuthListener _listener;

  /**
   * The call back implementation to use.
   */
  private final ClientManager _manager;

  /**
   * The call back implementation to use.
   */
  private final TokenFactory _factory;

  /**
   * Create a new configuration instance.
   * 
   * @param callbacks the OAuth listener implementation to use.
   * @param manager   the OAuth client manager implementation to use.
   * @param factory   the OAuth token factory implementation to use.
   * 
   * @throws NullPointerException Should any argument be <code>null</code>.
   */
  private OAuthConfig(OAuthListener listener, ClientManager manager, TokenFactory factory) {
    if (listener == null) throw new NullPointerException("listener");
    if (manager == null) throw new NullPointerException("manager");
    if (factory == null) throw new NullPointerException("factory");
    this._listener = listener;
    this._manager = manager;
    this._factory = factory;
  }

  /**
   * Returns the OAuth listener implementation to use.
   * 
   * @return the OAuth listener implementation to use.
   */
  public OAuthListener listener() {
    return this._listener;
  }

  /**
   * Returns the OAuth client manager implementation to use.
   * 
   * @return the OAuth client manager implementation to use.
   */
  public ClientManager manager() {
    return this._manager;
  }

  /**
   * Returns the OAuth token factory implementation to use.
   * 
   * @return the OAuth token factory implementation to use.
   */
  public TokenFactory factory() {
    return this._factory;
  }
  
  // Static helpers ------------------------------------------------------------------------------- 

  /**
   * Return the OAuth configuration.
   * 
   * <p>If the configuration has not been initialised, this method returns a NOP configuration.
   * 
   * @return This method always returns a configuration.
   */
  public static OAuthConfig getInstance() {
    if (config == null) nop();
    return config;
  }

  /**
   * Initialises the OAuth config using the specified implementations.
   * 
   * @param listener the OAuth listener implementation to use.
   * @param manager  the OAuth client manager implementation to use.
   * @param factory  the OAuth token factory implementation to use.
   * 
   * @throws NullPointerException if any argument is <code>null</code>.
   */
  public static void init(OAuthListener listener, ClientManager manager, TokenFactory factory) {
    config = new OAuthConfig(listener, manager, factory);
    OAuthTokens.init(config._factory);
  }

  /**
   * Initialises the OAuth config using the specified implementations.
   */
  private static void nop() {
    config = new OAuthConfig(new NOPListener(), new NOPClientManager(), new NOPTokenFactory());
    OAuthTokens.init(config._factory);
  }

}
