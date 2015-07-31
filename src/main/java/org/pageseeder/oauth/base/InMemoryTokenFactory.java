package org.pageseeder.oauth.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.pageseeder.oauth.OAuthCredentials;
import org.pageseeder.oauth.server.OAuthAccessToken;
import org.pageseeder.oauth.server.OAuthClient;
import org.pageseeder.oauth.server.TokenFactory;
import org.pageseeder.oauth.util.Strings;


/**
 * A simple token factory holding tokens in memory.
 * 
 * @author Christophe Lauret
 * @version 27 July 2011
 */
public final class InMemoryTokenFactory implements TokenFactory {

  /**
   * The defaut maximum age for an OAuth token.
   */
  private final static long DEFAULT_TOKEN_MAX_AGE = 24 * 3600 * 1000L;

  /**
   * To look up OAuth tokens by name.
   */
  private final Map<String, OAuthAccessToken> _tokens = new HashMap<String, OAuthAccessToken>();

  /**
   * The maximum that token can have.
   */
  private final long _maxAge;

  /**
   * Creates a new token factory setting the maximum age to 24 hours.
   */
  public InMemoryTokenFactory() {
    this._maxAge = DEFAULT_TOKEN_MAX_AGE;
  }

  /**
   * Creates a new token factory.
   * 
   * @param maxAge the maximum age that tokens created by this factory can have.
   */
  public InMemoryTokenFactory(long maxAge) {
    this._maxAge = maxAge;
  }
  
  /**
   * Return the specified OAuth token instance using the token string.
   * 
   * @param token the token string.
   * @return the corresponding OAuth token or <code>null</code>.
   */
  public OAuthAccessToken get(String token) {
    if (token == null) return null;
    return this._tokens.get(token);
  }

  /**
   * Returns the access tokens.
   * 
   * @param upTo The max number of tokens to return.
   * @return the access tokens.
   */
  public Collection<OAuthAccessToken> listTokens(int upTo) {
    if (upTo < 0) return listTokens();
    List<OAuthAccessToken> tokens = new ArrayList<OAuthAccessToken>(upTo);
    synchronized (this._tokens) {
      int count = 0;
      for (Iterator<OAuthAccessToken> i = _tokens.values().iterator(); count < upTo && i.hasNext(); count++) {
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
  public Collection<OAuthAccessToken> listTokens() {
    List<OAuthAccessToken> tokens = new ArrayList<OAuthAccessToken>(this._tokens.size());
    synchronized (this._tokens) {
      tokens.addAll(new ArrayList<OAuthAccessToken>(this._tokens.values()));
    }
    return tokens;
  }

  /**
   * Creates new token credentials for the specified client.
   * 
   * @param client The OAuth client for which this token is issued.
   * @return A new OAuth token.
   */
  public synchronized OAuthAccessToken newToken(OAuthClient client) {
    // Generate the token string and ensure it is unique
    String identifier = Strings.random(client.id(), 37);
    while (this._tokens.containsKey(identifier)) {
      identifier = Strings.random(client.id(), 21);
    }
    String secret = Strings.random(client.id(), 23);
    OAuthCredentials credentials = new OAuthCredentials(identifier, secret);
    long expires = System.currentTimeMillis() + this._maxAge;
    OAuthAccessToken token = new OAuthAccessToken(credentials, expires, client);
    this._tokens.put(identifier, token);
    return token;
  }

  /**
   * Remove the specified token effectively revoking access for the client currently using the token.
   * 
   * @return The token that was removed.
   */
  public synchronized OAuthAccessToken revoke(String token) {
    return this._tokens.remove(token);
  }

  /**
   * Remove all the tokens which are stale.
   * 
   * @return the number of tokens which were removed.
   */
  public synchronized int clearStale() {
    int count = 0;
    // Remove Access tokens which have expired
    Iterator<Entry<String, OAuthAccessToken>> i = this._tokens.entrySet().iterator();
    while (i.hasNext()) {
      OAuthAccessToken token = i.next().getValue();
      if (token.hasExpired()) {
        i.remove();
        count++;
      }
    }
    return count;
  }

}
