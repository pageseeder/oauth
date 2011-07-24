package com.weborganic.oauth;

/**
 * An enumeration of OAuth problems as defined by the Problem Reporting OAuth extension.
 * 
 * @see <a href="http://wiki.oauth.net/ProblemReporting">problem reporting</a>
 * 
 * @author Christophe Lauret
 * @version 20 July 2011
 */
public enum OAuthProblem {

  /**
   * The oauth_version isn't supported by the Service Provider.
   * 
   * <p>In this case, the response SHOULD also contain an <code>oauth_acceptable_versions</code> parameter.
   */
  version_rejected(400),

  /**
   * A required parameter wasn't received.
   * 
   * <p>In this case, the response SHOULD also contain an <code>oauth_parameters_absent</code> parameter.
   */
  parameter_absent(400),

  /**
   * An unexpected parameter was received.
   * 
   * <p>In this case, the response SHOULD also contain an <code>oauth_parameters_rejected</code> parameter.
   */
  parameter_rejected(400),

  /**
   * The <code>oauth_timestamp</code> value is unacceptable to the Service Provider.
   * 
   * <p>In this case, the response SHOULD also contain an <code>oauth_acceptable_timestamps</code> parameter.
   */
  timestamp_refused(400),

  /**
   * The oauth_nonce value was used in a previous request, and consequently can't be used now.
   */
  nonce_used(401),

  /**
   * The oauth_signature_method is unacceptable to the Service Provider.
   */
  signature_method_rejected(400),

  /**
   * The oauth_signature is invalid.
   * 
   * <p>That is, it doesn't match the signature computed by the Service Provider.
   */
  signature_invalid(400),

  /**
   * The <code>oauth_consumer_key</code> is unknown to the Service Provider.
   */
  consumer_key_unknown(400),

  /**
   * The <code>oauth_consumer_key</code> is permanently unacceptable to the Service Provider.
   * 
   * For example, the Consumer may be black listed.
   */
  consumer_key_rejected(400),

  /**
   * The oauth_consumer_key is temporarily unacceptable to the Service Provider.
   * 
   * <p>For example, the Service Provider may be throttling the Consumer.
   */
  consumer_key_refused(503),

  /**
   * The oauth_token has been consumed.
   * 
   * <p>That is, it can't be used any more because it has already been used in a previous request or requests.
   */
  token_used(401),

  /**
   * The oauth_token has expired.
   * 
   * <p>That is, it was issued too long ago to be used now.
   * <p>If the ScalableOAuth extensions are supported by the Consumer, it can pass on the
   * <code>oauth_session_handle</code> it received in the previous Access Token request to obtain 
   * a renewed Access token and secret.
   */
  token_expired(401),

  /**
   * The <code>oauth_token</code> has been revoked.
   * 
   * <p>That is, the Service Provider has unilaterally decided it will never accept this token.
   */
  token_revoked(401),

  /**
   * The oauth_token is unacceptable to the Service Provider.
   * 
   * <p>The reason is unspecified. It might mean that the token was never issued, or consumed or 
   * expired and then subsequently forgotten by the Service Provider.
   */
  token_rejected(401),

  /**
   * The <code>oauth_verifier</code> is incorrect.
   */
  verifier_invalid(401),
  
  /**
   * The user needs to give additional permissions before the Consumer is allowed access to the resource.
   * 
   * <p>If the ScalableOAuth extensions are supported by the Consumer, it can use the <code>oauth_token</code>
   * (access token) it already has as the request token to initiate the authorization process again,
   * in which case it must use the oauth_token_secret (access token secret) to sign the request for 
   * a new access token once the user finishes giving authorization.
   */
  additional_authorization_required(401),

  /**
   * The User hasn't decided whether to permit this Consumer to access Protected Resources.
   * 
   * <p>Usually happens when the Consumer requests Access Token before the user completes authorization process.
   */
  permission_unknown(401),

  /**
   * The User refused to permit this Consumer to access Protected Resources.
   */
  permission_denied(401),

  /**
   * The User (in most cases it's just user's IP) is temporarily unacceptable to the Service Provider.
   * 
   * <p>For example, the Service Provider may be rate limiting the IP based on number of requests.
   * <p>This error condition applies to the Authorization process where the user interacts with Service Provider directly.
   * <p>The Service Provider might return this error in the authorization response or in the Access Token request response.
   */
  user_refused(503);

  /**
   * The HTTP status corresponding to the problem.
   */
  private final int httpCode;

  /**
   * Create an OAuth problem to be associated with the specified HTTP status code.
   * 
   * @param status The status code to use for this problem.
   */
  private OAuthProblem(int status) {
    this.httpCode = status;
  }

  /**
   * @return The status code to use for this problem.
   */
  public int getHttpCode() {
    return httpCode;
  }
}
