package org.weborganic.oauth.signature;

import org.weborganic.oauth.util.Preconditions;
import org.weborganic.oauth.util.URLs;

/**
 * A plain text signer.
 * 
 * <p>Copied from RFC 5849: The OAuth 1.0 Protocol - 3.4.4. PLAINTEXT
 * <quote><pre>
 * The "PLAINTEXT" method does not employ a signature algorithm.  It
 * MUST be used with a transport-layer mechanism such as TLS or SSL (or
 * sent over a secure channel with equivalent protections).  It does not
 * utilize the signature base string or the "oauth_timestamp" and
 * "oauth_nonce" parameters.
 *
 * The "oauth_signature" protocol parameter is set to the concatenated
 * value of:
 *
 * 1.  The client shared-secret, after being encoded (Section 3.6).
 *
 * 2.  An "&" character (ASCII code 38), which MUST be included even
 *     when either secret is empty.
 *
 * 3.  The token shared-secret, after being encoded (Section 3.6).
 * </pre></quote>
 * 
 * @see <a href="http://tools.ietf.org/html/rfc5849#section-3.4.4">The OAuth 1.0 Protocol - 3.4.4. PLAINTEXT</a>
 * 
 * @author christophe
 * @version 21 July 2011
 */
public final class PlainTextSigner implements OAuthSigner {

  /**
   * Method for this signer.
   */
  private static final String METHOD = "PLAINTEXT";

  @Override
  public String getSignature(String baseString, String clientSecret, String tokenSecret) {
    Preconditions.checkEmptyString(clientSecret, "Client Secret cannot be null or empty string.");
    if (tokenSecret != null) {
      return URLs.encode(clientSecret) + '&' + URLs.encode(tokenSecret);
    } else {
      return URLs.encode(clientSecret) + '&';
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @return Always <code>"PLAINTEXT"</code>.
   */
  @Override
  public String getSignatureMethod() {
    return METHOD;
  }

}
