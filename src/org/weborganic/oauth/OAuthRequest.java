package org.weborganic.oauth;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.weborganic.oauth.util.URLs;

/**
 * Represents an OAuth request made my the client to the server.
 * 
 * @author Christophe Lauret
 * @version 28 October 2011
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
      return "OAuthMessage(" + method + ", " + baseURL + ", " + oauthParameters + ")";
  }

  public String getOAuthParameter(OAuthParameter name) throws IOException {
      return oauthParameters.get(name);
  }

  /**
   * Returns a map of OAuth parameters included in this message.
   *   
   * @return a map of OAuth parameters included in this message.
   */
  public Map<OAuthParameter, String> getOAuthParameters() {
    return oauthParameters;
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
   * @return
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
      if (out.length() != 0) out.append('&');
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
  public static OAuthRequest parse(HttpServletRequest req) throws OAuthException {
    // HTTP Method
    String method = req.getMethod().toUpperCase();
    String host = req.getHeader("Host");
    String baseURL = toBaseURL(req);
    // OAuth parameters
    String authorization = req.getHeader("Authorization");
    LOGGER.debug("Authorization => {}", authorization);
    Map<OAuthParameter, String> parameters = parseAuthorization(authorization);
    OAuthRequest m = new OAuthRequest(method, baseURL, req.getParameterMap(), parameters);
    LOGGER.debug("OAuthRequest => {}", m.toString());
    return m;
  }

  protected static String normalizeUrl(String url) throws URISyntaxException {
      URI uri = new URI(url);
      String scheme = uri.getScheme().toLowerCase();
      String authority = uri.getAuthority().toLowerCase();
      boolean dropPort = (scheme.equals("http") && uri.getPort() == 80)
                         || (scheme.equals("https") && uri.getPort() == 443);
      if (dropPort) {
          // find the last : in the authority
          int index = authority.lastIndexOf(":");
          if (index >= 0) {
              authority = authority.substring(0, index);
          }
      }
      String path = uri.getRawPath();
      if (path == null || path.length() <= 0) {
          path = "/"; // conforms to RFC 2616 section 3.2.2
      }
      // we know that there is no query and no fragment here.
      return scheme + "://" + authority + path;
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
   * 
   * @param authorization
   * @return
   * @throws ParseException
   */
  private static Map<OAuthParameter, String> parseAuthorization(String authorization) throws OAuthException {
    // No authorization -> permission denied
    if (authorization == null) {
      throw new OAuthException(OAuthProblem.permission_denied);
    }
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

    // TODO: return an error when needed
    return parameters;
  }

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
