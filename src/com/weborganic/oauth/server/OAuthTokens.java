package com.weborganic.oauth.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.weborganic.oauth.OAuthCredentials;
import com.weborganic.oauth.util.Strings;

/**
 * A utility class to manage OAuth tokens. 
 * 
 * @author Christophe Lauret
 * @version 20 July 2011
 */
public class OAuthTokens {

  /**
   * The defaut maximum age for an OAuth token.
   */
  private final static long DEFAULT_TOKEN_MAX_AGE = 24 * 3600 * 1000L;

  /**
   * The defaut maximum age for a temporary OAuth token.
   */
  private final static long DEFAULT_TEMPORARY_MAX_AGE = 10 * 60 * 1000L;

  /**
   * Digits (for verifiers)
   */
  private final static char[] DIGITS = "0123456789".toCharArray();

  /**
   * To look up OAuth tokens by name.
   */
  private final static Map<String, OAuthAccessToken> TOKENS = new HashMap<String, OAuthAccessToken>();

  /**
   * To look up OAuth tokens by name.
   */
  private final static Map<String, OAuthTemporaryToken> TEMPORARY = new HashMap<String, OAuthTemporaryToken>();

  /**
   * Return the specified OAuth token instance using the token string.
   * 
   * @param token the token string.
   * @return the corresponding OAuth token or <code>null</code>.
   */
  public static OAuthAccessToken get(String token) {
    if (token == null) return null;
    return TOKENS.get(token);
  }

  /**
   * Returns the access tokens.
   * 
   * @param upTo The max number of tokens to return.
   * @return the access tokens.
   */
  public static Collection<OAuthAccessToken> listTokens(int upTo) {
    if (upTo < 0) return listTokens();
    List<OAuthAccessToken> tokens = new ArrayList<OAuthAccessToken>(upTo);
    synchronized (TOKENS) {
      int count = 0;
      for (Iterator<OAuthAccessToken> i = TOKENS.values().iterator(); count < upTo && i.hasNext(); count++) {
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
  public static Collection<OAuthAccessToken> listTokens() {
    List<OAuthAccessToken> tokens = new ArrayList<OAuthAccessToken>(TOKENS.size());
    synchronized (TOKENS) {
      tokens.addAll(new ArrayList<OAuthAccessToken>(TOKENS.values()));
    }
    return tokens;
  }

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
   * Creates new token credentials for the specified client.
   * 
   * @param client The OAuth client for which this token is issued.
   * @return A new OAuth token.
   */
  public synchronized static OAuthAccessToken newToken(OAuthClientImpl client) {
    // Generate the token string and ensure it is unique
    String identifier = Strings.random(client.id(), 37);
    while (TOKENS.containsKey(identifier)) {
      identifier = Strings.random(client.id(), 21);
    }
    String secret = Strings.random(client.id(), 23);
    OAuthCredentials credentials = new OAuthCredentials(identifier, secret);
    long expires = System.currentTimeMillis() + DEFAULT_TOKEN_MAX_AGE;
    OAuthAccessToken token = new OAuthAccessToken(credentials, expires, client);
    TOKENS.put(identifier, token);
    return token;
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
  public synchronized static OAuthTemporaryToken newTemporary(OAuthClientImpl client, String callback) {
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
  public static synchronized OAuthAccessToken revoke(String token) {
    return TOKENS.remove(token);
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
    int count = 0;
    // Remove Access tokens which have expired
    Iterator<Entry<String, OAuthAccessToken>> i = TOKENS.entrySet().iterator();
    while (i.hasNext()) {
      OAuthAccessToken token = i.next().getValue();
      if (token.hasExpired()) {
        i.remove();
        count++;
      }
    }
    // Remove Temporary tokens which have expired or been used
    Iterator<Entry<String, OAuthTemporaryToken>> j = TEMPORARY.entrySet().iterator();
    while (j.hasNext()) {
      OAuthTemporaryToken token = j.next().getValue();
      if (token.hasExpired()) {
        j.remove();
        count++;
      }
    }
    return count;
  }

}
