package org.weborganic.oauth;


/**
 * This class is used for expected OAuth errors.
 * 
 * @author Christophe Lauret
 * @version 21 July 2011
 */
public class OAuthException extends Exception {

  /**
   * Serial version ID as per required by the Serializable interface.
   */
  private static final long serialVersionUID = 8438616517831564414L;

  /**
   * The wrapped OAuth problem
   */
  OAuthProblem p;

  public OAuthException(OAuthProblem p) {
    this.p = p;
  }

  public OAuthException(OAuthProblem p, Exception ex) {
    super(ex);
    this.p = p;
  }

  
  public OAuthProblem getProblem() {
    return p;
  }
}
