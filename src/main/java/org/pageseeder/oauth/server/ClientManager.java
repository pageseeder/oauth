package org.pageseeder.oauth.server;

/**
 * Implementations must 
 * 
 * @author Christophe Lauret
 * @version 24 October 2011
 */
public interface ClientManager {

  /**
   * Return the specified OAuth client by Client identifier (API key).
   * 
   * @param identifier the Client identifier.
   * @return the corresponding OAuth client or <code>null</code>.
   */
  OAuthClient getByKey(String identifier);

}
