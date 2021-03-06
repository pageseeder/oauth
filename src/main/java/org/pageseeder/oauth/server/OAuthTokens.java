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
package org.pageseeder.oauth.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.pageseeder.oauth.OAuthCredentials;
import org.pageseeder.oauth.util.Strings;


/**
 * A utility class to manage OAuth tokens.
 *
 * @author Christophe Lauret
 * @version 27 July 2011
 */
public class OAuthTokens {

  /**
   * The default maximum age for a temporary OAuth token.
   */
  private final static long DEFAULT_TEMPORARY_MAX_AGE = 10 * 60 * 1000L;

  /**
   * Digits (for verifiers)
   */
  private final static char[] DIGITS = "0123456789".toCharArray();

  /**
   * To look up OAuth tokens by name.
   */
  private static TokenFactory factory = null;

  /**
   * To look up OAuth tokens by name.
   */
  private final static Map<String, OAuthTemporaryToken> TEMPORARY = new HashMap<String, OAuthTemporaryToken>();

  /**
   * Returns the access tokens.
   *
   * @param upTo The max number of tokens to return.
   * @return the access tokens.
   */
  public static Collection<OAuthTemporaryToken> listTemporaryTokens(int upTo) {
    if (upTo < 0) return listTemporaryTokens();
    List<OAuthTemporaryToken> tokens = new ArrayList<OAuthTemporaryToken>(upTo);
    synchronized (TEMPORARY) {
      int count = 0;
      for (Iterator<OAuthTemporaryToken> i = TEMPORARY.values().iterator(); count < upTo && i.hasNext(); count++) {
        tokens.add(i.next());
      }
    }
    return tokens;
  }

  /**
   * Returns the access tokens.
   *
   * @return the access tokens.
   */
  public static Collection<OAuthTemporaryToken> listTemporaryTokens() {
    List<OAuthTemporaryToken> tokens = new ArrayList<OAuthTemporaryToken>(TEMPORARY.size());
    synchronized (TEMPORARY) {
      tokens.addAll(new ArrayList<OAuthTemporaryToken>(TEMPORARY.values()));
    }
    return tokens;
  }

  /**
   * Returns the specified OAuth token instance using the token string.
   *
   * @param token the token string.
   * @return the corresponding OAuth token or <code>null</code>.
   */
  public static OAuthTemporaryToken getTemporary(String token) {
    if (token == null) return null;
    return TEMPORARY.get(token);
  }

  /**
   * Creates new temporary credentials for the specified client with the given callback.
   *
   * @param client   The OAuth client for which this token is issued.
   * @param callback The URL to call back if the resource owner authorizes the client.
   * @return A new temporary token.
   */
  public synchronized static OAuthTemporaryToken newTemporary(OAuthClient client, String callback) {
    // Generate the token string and ensure it is unique
    String identifier = Strings.random(client.id(), 37);
    while (TEMPORARY.containsKey(identifier)) {
      identifier = Strings.random(client.id(), 21);
    }
    String secret = Strings.random(client.id(), 23);
    String verifier = Strings.random(7, DIGITS);
    OAuthCredentials credentials = new OAuthCredentials(identifier, secret);
    long expires = System.currentTimeMillis() + DEFAULT_TEMPORARY_MAX_AGE;
    OAuthTemporaryToken token = new OAuthTemporaryToken(credentials, client, verifier, expires, callback);
    TEMPORARY.put(identifier, token);
    return token;
  }

  /**
   * Remove the specified token effectively revoking access for the client currently using the token.
   *
   * @return The token that was removed.
   */
  public static synchronized OAuthTemporaryToken revokeTemporary(String token) {
    return TEMPORARY.remove(token);
  }

  /**
   * Remove all the tokens which are stale.
   *
   * @return the number of tokens which were removed.
   */
  public static synchronized int clearStale() {
    if (factory == null) throw new IllegalStateException("Token Factory was not initialised.");
    int count = factory.clearStale();
    // Remove Temporary tokens which have expired or been used
    Iterator<Entry<String, OAuthTemporaryToken>> j = TEMPORARY.entrySet().iterator();
    while (j.hasNext()) {
      OAuthTemporaryToken token = j.next().getValue();
      if (token.hasExpired() || token.isUsed()) {
        j.remove();
        count++;
      }
    }
    return count;
  }

  /**
   * Sets the token factory to use.
   *
   * @return the token factory to use.
   */
  protected static void init(TokenFactory factory) {
    OAuthTokens.factory = factory;
  }

  /**
   * Returns the token factory currently in use.
   *
   * @return the token factory currently in use.
   */
  public static TokenFactory getFactory() {
    return OAuthTokens.factory;
  }

}
