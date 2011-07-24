package com.weborganic.oauth.util;

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
