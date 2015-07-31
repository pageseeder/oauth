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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pageseeder.oauth.OAuthCredentials;
import org.pageseeder.oauth.server.ClientManager;
import org.pageseeder.oauth.server.OAuthClient;
import org.pageseeder.oauth.util.Strings;


/**
 * A utility class to manage OAuth clients.
 *
 * @author Christophe Lauret
 * @version 20 July 2011
 */
public final class InMemoryClientManager implements ClientManager {

  /**
   * To look up OAuth clients by name.
   */
  private final Map<String, OAuthClientImpl> clientsByName = new HashMap<String, OAuthClientImpl>();

  /**
   * To look up OAuth clients by API Key.
   */
  private final Map<String, OAuthClientImpl> clientsByIdentifier = new HashMap<String, OAuthClientImpl>();

  /*
  static {
    // Test client using callback URL
    OAuthClientImpl callback = new OAuthClientImpl("ClientTest-Callback", new OAuthCredentials("callback-key", "callback-secret"));
    callback.setCallbackURL("http://oauthclient.weborganic.com:8092/oauth/verify");
    register(callback);

    // Test client using Out Of Band configuration
    OAuthClientImpl oob = new OAuthClientImpl("ClientTest-OutOfBand", new OAuthCredentials("oob-key", "oob-secret"));
    register(oob);
  }
*/

  /**
   * Creates a new OAuth client with the specified name.
   *
   * @param name The name of the OAuth client.
   * @return a new OAuth client instance.
   */
  public OAuthClientImpl create(String name) {
    if (this.clientsByName.containsKey(name))
      throw new IllegalArgumentException("A client with the same name already exists.");
    // Generate the key and ensure it is unique
    String identifier = Strings.random(name, 21);
    while (this.clientsByIdentifier.containsKey(identifier)) {
      identifier = Strings.random(name, 21);
    }
    String secret = Strings.random(name, 37);
    OAuthCredentials credentials = new OAuthCredentials(identifier, secret);
    return new OAuthClientImpl(name, credentials);
  }

  /**
   * Reset the keys for the specified client.
   *
   * @param client The OAuth client.
   */
  public synchronized void reset(OAuthClientImpl client) {
    String oldIdentifier = client.getCredentials().identifier();
    // Generate the key and ensure it is unique
    String newIdentifier = Strings.random(client.id(), 21);
    while (this.clientsByIdentifier.containsKey(newIdentifier)) {
      newIdentifier = Strings.random(client.id(), 21);
    }
    String secret = Strings.random(client.id(), 37);
    OAuthCredentials credentials = new OAuthCredentials(newIdentifier, secret);
    client.setCredentials(credentials);
    // If registered, update the details
    if (this.clientsByIdentifier.remove(oldIdentifier) != null) {
      this.clientsByIdentifier.put(newIdentifier, client);
    }
  }

  /**
   * Registers the specified client.
   *
   * @param client The OAuth client to register.
   */
  public synchronized void register(OAuthClientImpl client) {
    if (this.clientsByName.containsKey(client.id()))
      throw new IllegalArgumentException("A client with the same name has already been registered.");
    if (this.clientsByIdentifier.containsKey(client.getCredentials().identifier()))
      throw new IllegalArgumentException("A client with the same key has already been registered.");
    this.clientsByName.put(client.id(), client);
    // TODO clashing keys
    this.clientsByIdentifier.put(client.getCredentials().identifier(), client);
  }

  /**
   * Return the specified OAuth client by name.
   *
   * @param name the name of the OAuth client.
   * @return the corresponding OAuth client or <code>null</code>.
   */
  public OAuthClient getByName(String name) {
    return this.clientsByName.get(name);
  }

  /**
   * Return the specified OAuth client by Client identifier (API key).
   *
   * @param identifier the Client identifier.
   * @return the corresponding OAuth client or <code>null</code>.
   */
  @Override
  public OAuthClientImpl getByKey(String identifier) {
    return this.clientsByIdentifier.get(identifier);
  }

  /**
   * Return the specified OAuth client by API key.
   *
   * @param key the API key of the OAuth client.
   * @return the corresponding OAuth client or <code>null</code>.
   */
  public List<OAuthClientImpl> listRegisteredClients() {
    return new ArrayList<OAuthClientImpl>(this.clientsByIdentifier.values());
  }

}
