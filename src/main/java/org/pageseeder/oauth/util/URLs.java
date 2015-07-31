package org.pageseeder.oauth.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Pattern;

import org.pageseeder.oauth.OAuthConstants;


/**
 * A utility class to perform basic OAuth operations.
 * 
 * @author Christophe Lauret
 * @version 20 July 2011
 */
public class URLs {

  /**
   * Pattern matching valid URLs.
   */
  private static final Pattern A_VALID_URL = Pattern.compile("[a-zA-Z_-]+://\\S+");

  /** Utility class. */
  private URLs() {
  }

  /**
   * Translates a string into <code>application/x-www-form-urlencoded</code> format as per 
   * requirement by OAuth 1.0. 
   * 
   * <p>As OAuth encodes some characters differently:
   * <ul>
   *   <li>' ' (ASCII code 32) must be should be encoded as "%20" not '+';</li>
   *   <li>'*' (ASCII code 42) must be encoded as "%2A";</li>
   *   <li>'~' (ASCII code 126) mustnot be encoded.</li>
   * </ul>
   * 
   * @param s The string to percent encode.
   * @return the encoded string
   */
  public static String encode(String s) {
    if (s == null) return null;
    try {
      return URLEncoder.encode(s, "utf-8").replace("+", "%20").replace("*", "%2A").replace("%7E", "~");
    } catch (UnsupportedEncodingException ex) {
      // Theoratically, this will never happen...
      throw new IllegalStateException(ex.getMessage(), ex);
    }
  }

  /**
   * Decode the specified URL string. 
   * 
   * @param s The string to percent encode.
   * @return the encoded string
   */
  public static String decode(String s) {
    try {
      return URLDecoder.decode(s, "utf-8");
    } catch (java.io.UnsupportedEncodingException ex) {
      // Theoratically, this will never happen...
      throw new IllegalStateException(ex.getMessage(), ex);
    }
  }

  /**
   * Indicates whether the specified callback URL is acceptable.
   * 
   * <p>A valid callback URL, MUST be:
   * <ul>
   *   <li>either a valid absolute URI not containing a fragment part</li>
   *   <li>OR the <i>Out of Band</i> string <code>"oob"</code> if the client is unable to receive callbacks.</li>
   * </ul>
   *
   * @param url The callback URL to check.
   * @return <code>true</code>  if the callback URL is valid;
   *         <code>false</code> otherwise.
   */
  public static boolean isValidCallback(String url) {
    if (OAuthConstants.OUT_OF_BAND.equals(url)) return true;
    if (url.indexOf('#') > 0) return false;
    return A_VALID_URL.matcher(url).matches();
  }

}
