package org.weborganic.oauth.server;

import java.util.List;

import org.weborganic.oauth.OAuthCredentials;

/**
 * A utility class to manage OAuth clients. 
 * 
 * @author Christophe Lauret
 * @version 20 July 2011
 */
public final class OAuthClients {

  /**
   * To look up OAuth clients by name.
   */
  private final static InMemoryClientManager INMEMORY = new InMemoryClientManager();

  /**
   * To look up OAuth clients by name.
   */
  private static ClientManager manager = INMEMORY;

  static {
    // Test client using callback URL
    OAuthClientImpl callback = new OAuthClientImpl("ClientTest-Callback", new OAuthCredentials("callback-key", "callback-secret"));
    callback.setCallbackURL("http://oauthclient.weborganic.com:8092/oauth/verify");
    register(callback);

    // Test client using Out Of Band configuration
    OAuthClientImpl oob = new OAuthClientImpl("ClientTest-OutOfBand", new OAuthCredentials("oob-key", "oob-secret"));
    register(oob);
  }

  /**
   * Sets the OAuth client manager this class should use.
   * 
   * @param manager the OAuth client manager this class should use.
   */
  public static void init(ClientManager manager) {
    OAuthClients.manager = manager;
  }

  /**
   * Creates a new OAuth client with the specified name.
   *  
   * @param name The name of the OAuth client.
   * @return a new OAuth client instance.
   */
  public static OAuthClientImpl create(String name) {
    return INMEMORY.create(name);
  }

  /**
   * Reset the keys for the specified client.
   * 
   * @param client The OAuth client.
   */
  public static synchronized void reset(OAuthClientImpl client) {
    INMEMORY.reset(client);
  }

  /**
   * Registers the specified client.
   *
   * @param client The OAuth client to register.
   */
  public static synchronized void register(OAuthClientImpl client) {
    INMEMORY.register(client);
  }

  /**
   * Return the specified OAuth client by name.
   * 
   * @param name the name of the OAuth client.
   * @return the corresponding OAuth client or <code>null</code>.
   */
  public static OAuthClient getByName(String name) {
    return INMEMORY.getByName(name);
  }

  /**
   * Return the specified OAuth client by Client identifier (API key).
   * 
   * @param identifier the Client identifier.
   * @return the corresponding OAuth client or <code>null</code>.
   */
  public static OAuthClient getByKey(String identifier) {
    return manager.getByKey(identifier);
  }

  /**
   * Return the specified OAuth client by API key.
   * 
   * @param key the API key of the OAuth client.
   * @return the corresponding OAuth client or <code>null</code>.
   */
  public static List<OAuthClientImpl> listRegisteredClients() {
    return INMEMORY.listRegisteredClients();
  }

  /**
   * Returns the manager used by this class.
   * 
   * @param manager the OAuth clients manager used by this class.
   */
  public static ClientManager getManager() {
    return manager;
  }

}
