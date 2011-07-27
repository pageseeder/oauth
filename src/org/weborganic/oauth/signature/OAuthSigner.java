package org.weborganic.oauth.signature;

/**
 * Classes implementing this interface are capable of signing and checking signatures
 * 
 * <p>Implementations MUST ensure that there is a <code>public</code> constructor taking no 
 * arguments in order to be able to register signers.
 * 
 * @author Christophe Lauret
 * @version 20 July 2011
 */
public interface OAuthSigner {

  /**
   * Returns the signature for the specified arguments.
   * 
   * @param baseString   A URL-encoded base string to sign.
   * @param clientSecret The OAuth client secret.
   * @param tokenSecret  The OAuth token secret (may be <code>null</code>).
   * 
   * @return the corresponding signature.
   */
  public String getSignature(String baseString, String clientSecret, String tokenSecret);

  /**
   * Returns the signature method.
   * 
   * @see <a href="http://tools.ietf.org/html/rfc5849#section-3.4">The OAuth 1.0 Protocol - 3.4. Signature</a>
   * 
   * @return the OAuth signature method.
   */
  public String getSignatureMethod();

}
