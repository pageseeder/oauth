package org.weborganic.oauth.server;

import org.weborganic.oauth.base.InMemoryClientManager;
import org.weborganic.oauth.base.InMemoryTokenFactory;
import org.weborganic.oauth.base.NOPCallbacks;


/**
 * Centralise the OAuth server configuration in one class.
 * 
 * <p>The servlets and filters use this class to manage tokens and clients as well as invoke
 * the call back methods at each end point. 
 * 
 * @author Christophe Lauret
 * @version 26 October 2011
 */
public final class OAuthConfig {

  /**
   * The configuration to use.
   */
  private static OAuthConfig config = null;

  /**
   * The call back implementation to use.
   */
  private final Callbacks _callbacks;

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
   * @param callbacks the OAuth callbacks implementation to use.
   * @param manager   the OAuth client manager implementation to use.
   * @param factory   the OAuth token factory implementation to use.
   */
  private OAuthConfig(Callbacks callbacks, ClientManager manager, TokenFactory factory) {
    if (callbacks == null) throw new NullPointerException("callbacks");
    if (manager == null) throw new NullPointerException("manager");
    if (factory == null) throw new NullPointerException("factory");
    this._callbacks = callbacks;
    this._manager = manager;
    this._factory = factory;
  }

  /**
   * Returns the OAuth call backs implementation to use.
   * 
   * @return the OAuth call backs implementation to use.
   */
  public Callbacks callbacks() {
    return this._callbacks;
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
   * 
   */
  public static OAuthConfig getInstance() {
    return config;
  }

  /**
   * Initialises the OAuth config using the specified implementations.
   * 
   * @param callbacks the OAuth callbacks implementation to use.
   * @param manager   the OAuth client manager implementation to use.
   * @param factory   the OAuth token factory implementation to use.
   * 
   * @throws NullPointerException if any argument is <code>null</code>.
   */
  public static void init(Callbacks callbacks, ClientManager manager, TokenFactory factory) {
    config = new OAuthConfig(callbacks, manager, factory);
    OAuthTokens.init(config._factory);
  }

  /**
   * Initialises the OAuth config using the specified implementations.
   */
  public static void init() {
    config = new OAuthConfig(new NOPCallbacks(), new InMemoryClientManager(), new InMemoryTokenFactory());
    OAuthTokens.init(config._factory);
  }

}
