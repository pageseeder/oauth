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
package org.pageseeder.oauth.util;

/**
 * A utility class to check that methods preconditions are met.
 *
 * <p>This class is primarily designed to detect programming errors by ensuring all preconditions
 * are met for a method to run and throwing a {@link RuntimeException} when the conditions are not
 * met so that the method fails fast and early.
 *
 * <p>Methods will throw the appropriate runtime exception depending on the nature of the error:
 * <ul>
 *   <li><code>NullPointerException</code> when an object is <code>null</code>;</li>
 *   <li><code>IllegalArgumentException</code> when an object does not match the requirements
 *   for the method.</li>
 * </ul>
 *
 * @author Christophe Lauret
 * @version 21 July 2011
 */
public final class Preconditions {

  /**
   * Utility class.
   */
  private Preconditions() {
  }

  /**
   * Checks that the specified object is not <code>null</code>.
   *
   * @param object any object
   * @param errorMsg error message
   *
   * @throws NullPointerException if the object is {@link NullPointerException}
   */
  public static void checkNotNull(Object o, String message) {
    if (o == null) throw new NullPointerException(message);
  }

  /**
   * Checks that a string is not <code>null</code> or empty.
   *
   * @param string any string
   * @param errorMsg error message
   *
   * @throws NullPointerException     If the string is <code>null</code>
   * @throws IllegalArgumentException if the string is empty
   */
  public static void checkEmptyString(String string, String message) {
    if (string == null) throw new NullPointerException(message);
    if (string.length() <= 0) throw new IllegalArgumentException(message);
  }

}
