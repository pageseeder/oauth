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
package org.pageseeder.oauth.signature;

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
