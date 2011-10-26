package org.weborganic.oauth.server;

/**
 * A utility class to manage OAuth clients. 
 * 
 * @author Christophe Lauret
 * @version 26 October 2011
 */
public final class OAuthClients {

  /**
   * Return the specified OAuth client by Client identifier (API key).
   * 
   * @deprecated Use {@link OAuthConfig#manager()} instead
   * 
   * @param identifier the Client identifier.
   * @return the corresponding OAuth client or <code>null</code>.
   */
  public static OAuthClient getByKey(String identifier) {
    return OAuthConfig.getInstance().manager().getByKey(identifier);
  }

  /**
   * Returns the manager used by this class.
   * 
   * @deprecated Use {@link OAuthConfig#manager()} instead
   * 
   * @param manager the OAuth clients manager used by this class.
   */
  public static ClientManager getManager() {
    return OAuthConfig.getInstance().manager();
  }

}
