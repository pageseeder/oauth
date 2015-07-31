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
    return this.p;
  }
}
