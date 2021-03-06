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
package org.pageseeder.oauth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.pageseeder.oauth.util.URLs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents an OAuth request made my the client to the server.
 *
 * @author Christophe Lauret
 * @version 29 November 2011
 */
public class OAuthRequest {

  /**
   * Logger.
   */
  private final static Logger LOGGER = LoggerFactory.getLogger(OAuthRequest.class);

  /**
   * The type of request
   */
  public enum Type {

    /**
     * A request to obtain temporary credentials.
     */
    temporary,

    /**
     * A request to obtain token credentials.
     */
    token,

    /**
     * A request to obtain access to a resource.
     */
    resource
  }

  /**
   * The HTTP Method normalised as per RFC 5849 (always upper case).
   */
  private final String method;

  /**
   * The base string URI as define in RFC 5849 - 3.4.1.2.
   */
  private final String baseURL;

  /**
   * The HTTP request parameters from either the query string or the content.
   */
  private final Map<String, String[]> httpParameters;

  /**
   * The OAuth parameters received.
   */
  private final Map<OAuthParameter, String> oauthParameters;

  /**
   * Create the new OAuthMessage with raw arguments.
   *
   * @param method          The HTTP method
   * @param baseURL         The base URL
   * @param httpParameters  The HTTP parameters.
   * @param oauthParameters The OAuth parameters (from Authorization header)
   */
  public OAuthRequest(String method, String baseURL, Map<String, String[]> httpParameters, Map<OAuthParameter, String> oauthParameters) {
    this.method = method;
    this.baseURL = baseURL;
    this.httpParameters = httpParameters;
    this.oauthParameters = oauthParameters;
  }

  @Override
  public String toString() {
      return "OAuthMessage(" + this.method + ", " + this.baseURL + ", " + this.oauthParameters + ")";
  }

  /**
   * Returns the value of the OAuth parameter
   *
   * @param name the name of the oauth parameter
   * @return the corresponding value if specified or <code>null</code>
   * @throws IOException
   */
  public String getOAuthParameter(OAuthParameter name) throws IOException {
      return this.oauthParameters.get(name);
  }

  /**
   * Returns a map of OAuth parameters included in this message.
   *
   * @return a map of OAuth parameters included in this message.
   */
  public Map<OAuthParameter, String> getOAuthParameters() {
    return this.oauthParameters;
  }

  /**
   * Signs this message with the specified signature.
   *
   * <p>A message can only be signed once, check if this message already contains an
   * <code>oauth_signature</code> before calling this method.
   *
   * @param signature The signature for this message.
   *
   * @throws IllegalStateException If the message has already been signed.
   */
  public void sign(String signature) {
    if (this.oauthParameters.containsKey(OAuthParameter.oauth_signature))
      throw new IllegalStateException("Already signed!");
    this.oauthParameters.put(OAuthParameter.oauth_signature, signature);
  }

  // Signature Base String
  // ==============================================================================================

  /**
   * Returns the signature base string for this message following the rules defined in the 3.4.1
   *
   * @see <a href="http://tools.ietf.org/html/rfc5849#section-3.4.1">3.4.1 - Signature Base String</a>
   *
   * @return the signature base string for this message.
   */
  public String toSignatureBaseString() {
    StringBuilder base = new StringBuilder();
    base.append(this.method);
    base.append('&');
    base.append(URLs.encode(this.baseURL));
    base.append('&');
    base.append(URLs.encode(getNormalisedParameters()));
    return base.toString();
  }

  /**
   * Returns the normalised parameters.
   *
   * @return the normalised parameters.
   */
  private String getNormalisedParameters() {
    // Normalise the parameters (this size is correct most of the time)
    List<Pair> pairs = new ArrayList<Pair>(this.httpParameters.size()+this.oauthParameters.size()-1);
    // Add the OAuth parameters
    for (Entry<OAuthParameter, String> e : this.oauthParameters.entrySet()) {
      if (e.getKey() != OAuthParameter.oauth_signature) {
        // no need to encode the oauth parameter names
        pairs.add(new Pair(e.getKey().name(), URLs.encode(e.getValue())));
      }
    }
    // Add the HTTP parameters
    for (Entry<String, String[]> e : this.httpParameters.entrySet()) {
      for (String v : e.getValue()) {
        pairs.add(new Pair(URLs.encode(e.getKey()), URLs.encode(v)));
      }
    }
    // Sort
    Collections.sort(pairs);
    //
    StringBuilder out = new StringBuilder();
    for (Pair pair : pairs) {
      if (out.length() != 0) {
        out.append('&');
      }
      out.append(pair.name());
      out.append('=');
      out.append(pair.value());
    }
    return out.toString();
  }

  /**
   * Checks that the current map contains all required
   */
  public void checkRequired(Set<OAuthParameter> required) throws OAuthException {
    for (OAuthParameter p : required) {
      if (!this.oauthParameters.containsKey(p)) throw new OAuthException(OAuthProblem.parameter_absent);
    }
  }

  // Parsers
  // ==============================================================================================

  /**
   * Constructs an OAuth message from the specified HTTP Servlet request.
   *
   * @param req HTTP Servlet request.
   *
   * @return the corresponding OAuth message.
   */
  @SuppressWarnings("unchecked")
  public static OAuthRequest parse(HttpServletRequest req) throws OAuthException {
    // HTTP Method
    String method = req.getMethod().toUpperCase();
    String baseURL = toBaseURL(req);
    // OAuth parameters
    String authorization = req.getHeader("Authorization");
    LOGGER.debug("Authorization => {}", authorization);
    Map<OAuthParameter, String> parameters = parseAuthorization(authorization);
    OAuthRequest m = new OAuthRequest(method, baseURL, req.getParameterMap(), parameters);
    LOGGER.debug("OAuthRequest => {}", m.toString());
    return m;
  }

  /**
   * Returns the base URL corresponding to the specified Servlet request.
   *
   * @param req The HTTP Servlet request.
   * @return The corresponding base URL.
   */
  private static String toBaseURL(HttpServletRequest req) {
    String scheme = req.getScheme().toLowerCase();
    String host = req.getServerName().toLowerCase();
    int port = req.getServerPort();
    String path = req.getRequestURI();
    boolean noPort = ("http".equals(scheme) && port == 80) || ("https".equals(scheme) && port == 443);
    String authority = noPort? host : host + ':' + port;
    return scheme + "://" + authority + path;
  }

  /**
   * Parses the Authorization header and returns the map of OAuth parameters.
   *
   * @param authorization the Authorization header.
   *
   * @return The OAuth parameters specified in the Authorization header.
   *
   * @throws OAuthException Either permission_denied or parameter_rejected
   */
  private static Map<OAuthParameter, String> parseAuthorization(String authorization) throws OAuthException {
    // No authorization -> permission denied
    if (authorization == null) throw new OAuthException(OAuthProblem.permission_denied);
    Map<OAuthParameter, String> parameters = new EnumMap<OAuthParameter, String>(OAuthParameter.class);
    String auth = authorization;
    if (authorization.toLowerCase().startsWith("oauth")) {
      auth = authorization.substring(5);
     // throw new ParseException("Authorization does not start with OAuth", 0);
    }
    for (String pair : auth.split(",")) {
      int eq = pair.indexOf('=');
      if (eq > 0) {
        String name = pair.substring(0, eq).trim();
        String value = URLs.decode(unquote(pair.substring(eq+1).trim()));
        OAuthParameter p = OAuthParameter.valueOf(name);
        // Duplicate parameters are not allowed
        if (parameters.containsKey(p))
          throw new OAuthException(OAuthProblem.parameter_rejected);
        parameters.put(p, value);
      }
    }

    return parameters;
  }

  /**
   * Removes quotes if any.
   *
   * @param v the value to unquote.
   * @return the value without quotes.
   */
  private static String unquote(String v) {
    if (v.length() < 2) return v;
    if (v.charAt(0) == '"' && v.charAt(v.length()-1) == '"') return v.substring(1, v.length()-1);
    return v;
  }

  /**
   * An encoded name-value pair used for parameter normalisation.
   *
   * @author Christophe Lauret
   * @version 20 July 2011
   */
  private static class Pair implements Comparable<Pair> {

    /** The percent encoded parameter name. */
    private final String name;

    /** The percent encoded parameter value. */
    private final String value;

    /**
     * Creates a new pair - ensure that both arguments are already encoded.
     *
     * @param name  The percent encoded parameter name
     * @param value The percent encoded parameter value.
     */
    public Pair(String name, String value){
      this.name = name;
      this.value = value;
    }

    /** @return The percent encoded parameter name. */
    public String name() {
      return this.name;
    }

    /** @return The percent encoded parameter value. */
    public String value() {
      return this.value;
    }

    @Override
    public int hashCode() {
      return 7*this.name.hashCode() + 31*this.value.hashCode();
    }

    @Override
    public boolean equals(Object o) {
      if (o == null) return false;
      if (this == o) return true;
      if (o.getClass() != this.getClass()) return false;
      Pair p = (Pair)o;
      return this.name.equals(p.name) && this.value.equals(p.value);
    }

    @Override
    public int compareTo(Pair o) {
      int n = this.name.compareTo(o.name);
      if (n != 0) return n;
      int v = this.value.compareTo(o.value);
      return v;
    }
  }

}
