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
/**
 * This package contains classes required for using OAuth in a Java Servlet environment.
 *
 * <p>It provides a servlet for each of the OAuth protocol endpoints:
 * <ul>
 *   <li><code>OAuthInitiateServlet</code> to provide temporary credentials;</li>
 *   <li><code>OAuthAuthorizeServlet</code> to accept the resource owner authorization;</li>
 *   <li><code>OAuthTokenServlet</code> to provide token credentials;</li>
 * </ul>
 *
 * <p>All three Servlets must be defined in the Web descriptor, see individual Servlets
 * documentation for configuration details.
 *
 * <p>The {@link org.pageseeder.oauth.servlet.OAuthInitiateServlet} and
 * {@link org.pageseeder.oauth.servlet.OAuthTokenServlet} can be
 *
 * <p>Unless the API allows applications to register publicly, the
 * {@link org.pageseeder.oauth.servlet.OAuthAuthorizeServlet} should be ALWAYS protected and
 * required authentication by the resource owner.
 *
 * <p>This package also provides a filter to provide access to resource via OAuth.
 */
package org.pageseeder.oauth.servlet;
