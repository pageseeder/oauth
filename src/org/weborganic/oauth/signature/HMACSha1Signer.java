package org.weborganic.oauth.signature;

import java.security.GeneralSecurityException;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.weborganic.oauth.util.Preconditions;
import org.weborganic.oauth.util.URLs;


/**
 * The HMAC-SHA1 signer.
 * 
 * <p>Copied from RFC 5849: The OAuth 1.0 Protocol - 3.4.2. HMAC-SHA1
 * <quote><pre>
 * The "HMAC-SHA1" signature method uses the HMAC-SHA1 signature
 * algorithm as defined in [RFC2104]:
 *
 *   digest = HMAC-SHA1 (key, text)
 *
 * The HMAC-SHA1 function variables are used in following way:
 *
 * text    is set to the value of the signature base string from
 *          Section 3.4.1.1.
 *
 * key     is set to the concatenated values of:
 *
 *         1.  The client shared-secret, after being encoded
 *             (Section 3.6).
 *
 *         2.  An "&" character (ASCII code 38), which MUST be included
 *             even when either secret is empty.
 *
 *         3.  The token shared-secret, after being encoded
 *             (Section 3.6).
 *
 * digest  is used to set the value of the "oauth_signature" protocol
 *         parameter, after the result octet string is base64-encoded
 *         per [RFC2045], Section 6.8.
 * </pre></quote>
 * 
 * @see <a href="http://tools.ietf.org/html/rfc5849#section-3.4.4">The OAuth 1.0 Protocol - 3.4.4. PLAINTEXT</a>
 * 
 * @author christophe
 * @version 21 July 2011
 */
public final class HMACSha1Signer implements OAuthSigner {

  /**
   * The MAC (Message Authentication Code) to use for this signer.
   */
  private static final String MAC_NAME = "HmacSHA1";

  /**
   * The signature method defined by OAuth.
   */
  private static final String METHOD = "HMAC-SHA1";

  /**
   * Creates a new signer
   */
  public HMACSha1Signer() {
  }

  /**
   * {@inheritDoc}
   * 
   * @throws IllegalStateException 
   */
  @Override
  public String getSignature(String baseString, String clientSecret, String tokenSecret) {
    Preconditions.checkEmptyString(baseString, "Base String cannot be null or empty string.");
    Preconditions.checkEmptyString(clientSecret, "Client Secret cannot be null or empty string.");
    try {
      byte[] data = computeSignature(baseString, clientSecret, tokenSecret);
      String signature = Base64.encodeBase64String(data);
      return signature;
    } catch (GeneralSecurityException ex) {
      // We're in trouble!
      throw new IllegalStateException(ex);
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @return Always <code>"HMAC-SHA1"</code>.
   */
  @Override
  public String getSignatureMethod() {
    return METHOD;
  }

  // Private helpers
  // ----------------------------------------------------------------------------------------------

  /**
   * Computes the signature as a byte array.
   * 
   * @param baseString
   * @param clientSecret
   * @param tokenSecret
   * 
   * @return The corresponding key.
   * 
   * @throws GeneralSecurityException
   */
  private byte[] computeSignature(String baseString, String clientSecret, String tokenSecret) throws GeneralSecurityException {
    SecretKey key = getKey(clientSecret, tokenSecret);
    Mac mac = Mac.getInstance(MAC_NAME);
    mac.init(key);
    byte[] text = StringUtils.getBytesUtf8(baseString);
    return mac.doFinal(text);
  }

  /**
   * Returns the key for the specified arguments.
   * 
   * @param clientSecret The client secret
   * @param tokenSecret  The token secret
   * 
   * @return The secret key
   */
  private SecretKey getKey(String clientSecret, String tokenSecret) {
    String keyString = tokenSecret != null
                     ? URLs.encode(clientSecret) + '&' + URLs.encode(tokenSecret)
                     : URLs.encode(clientSecret) + '&';
     byte[] keyBytes = StringUtils.getBytesUtf8(keyString);
     return new SecretKeySpec(keyBytes, MAC_NAME);
  }

}
