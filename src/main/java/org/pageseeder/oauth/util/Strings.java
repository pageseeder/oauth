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

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * A utility class for strings.
 *
 * <p>This class provides methods for:
 * <ul>
 *   <li>Generating pseudo-random strings for use by OAuth as tokens or keys.</li>
 *   <li>Checking string equality without</li>
 * </ul>
 *
 * <p>For convenience, this class will only return keys which characters do not require
 * URL percent encoding as they are made of ASCII letters and digits.
 *
 * <p>Note: The characters are not equally distributed, in other words som characters to appear
 * more frequently than others.
 *
 * @author Christophe Lauret
 * @version 21 July 2011
 */
public final class Strings {

  /**
   * Used to generate the keys at random.
   */
  private static final Random RANDOM = new Random();

  /**
   * Alpha numeric characters (Mixed case).
   */
  private static char[] ALPHA_NUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890".toCharArray();

  /**
   * Determine whether the given strings contain the same sequence of characters.
   *
   * <p>The implementation discourages a timing attack.
   *
   * @see <a href="http://codahale.com/a-lesson-in-timing-attacks/">A lesson in timing attack</a>
   *
   * @param x The first string to test for equality.
   * @param y The second string to test for equality.
   *
   * @return the results string equality.
   */
  public static boolean equals(String x, String y) {
    // Preconditions
    if (x == null) return y == null;
    if (y == null) return false;
    if (y.length() <= 0) return x.length() <= 0;
    // Let's compare
    char[] a = x.toCharArray();
    char[] b = y.toCharArray();
    char diff = (char) ((a.length == b.length) ? 0 : 1);
    int j = 0;
    for (char element : a) {
      diff |= element ^ b[j];
      j = (j + 1) % b.length;
    }
    return diff == 0;
  }


  /**
   *Generates a random string with the specified length and composed of alpha-numeric ASCII characters (mixed case).
   *
   * @param lenth The length of the random string.
   * @return a random string with the specified length and composed of alpha-numeric ASCII characters (mixed case).
   */
  public static String random(int length) {
    return random(length, ALPHA_NUMERIC);
  }

  /**
   * Generates a random string with the specified length and composed of alpha-numeric ASCII characters (mixed case).
   *
   * <p>This method uses the SHA1 algorithm; the returned key is always 20 chars.
   *
   * @param name A name to use.
   *
   * @return A 20 characters long random string and composed of alpha-numeric ASCII characters (mixed case).
   */
  public static String random(String name) {
    return random(name, ALPHA_NUMERIC);
  }

  /**
   * Generates a random string with the specified length and composed of alpha-numeric ASCII characters (mixed case).
   *
   * <p>The first characters (up to 20) are created using SHA1, if the specified length is greater
   * than 20, then the key is filled with random alpha numeric characters.
   *
   * @param name   A name to use
   * @param length The length of the key to generate.
   *
   * @return a key for the specified name and length.
   */
  public static String random(String name, int length) {
    return random(name, length, ALPHA_NUMERIC);
  }

  /**
   * Generates a random string with the specified length and composed of the characters within the specified range.
   *
   * @param name  A name to use
   * @param range The range of characters to use.
   *
   * @return a random string with the specified length and composed of the characters within the specified range.
   *
   * @throws NullPointerException     If the specified character range is <code>null</code>.
   * @throws IllegalArgumentException If the specified character range is less than 2 characters long.
   */
  public static String random(int length, char[] range) {
    checkRange(range);
    StringBuilder key = new StringBuilder();
    append(key, length, range);
    return key.toString();
  }

  /**
   * Generate a random string with the specified length and composed of the characters within the specified range.
   *
   * <p>This method uses the SHA1 algorithm; the returned string is always 20 characters long.
   *
   * @param name A name to use.
   * @param range The range of characters to use.
   *
   * @return a random string with the specified length and composed of the characters within the specified range.
   *
   * @throws NullPointerException     If the specified character range is <code>null</code>.
   * @throws IllegalArgumentException If the specified character range is less than 2 characters long.
   */
  public static String random(String name, char[] range) {
    checkRange(range);
    StringBuilder key = new StringBuilder();
    append(key, name, range);
    return key.toString();
  }

  /**
   * Generate a key for the given name and length.
   *
   * <p>The first characters (up to 20) are created using SHA1, if the specified length is greater
   * than 20, then the key is filled with random characters.
   *
   * @param name   A name to use
   * @param length The length of the key to generate.
   * @param range The range of characters to use.
   *
   * @return a random string with the specified length and composed of the characters within the specified range.
   *
   * @throws NullPointerException     If the specified character range is <code>null</code>.
   * @throws IllegalArgumentException If the specified character range is less than 2 characters long.
   */
  public static String random(String name, int length, char[] range) {
    checkRange(range);
    StringBuilder key = new StringBuilder();
    append(key, name, range);
    // extra chars
    if (key.length() >= length) {
      key.setLength(length);
    } else {
      append(key, length - key.length(), range);
    }
    return key.toString();
  }

  // Private helpers
  // ---------------------------------------------------------------------------------------------

  /**
   * Checks that the range parameter is valid.
   *
   * @throws NullPointerException If <code>null</code>.
   * @throws IllegalArgumentException If less than 2 characters long.
   */
  private static void checkRange(char[] range) {
    if (range == null) throw new NullPointerException("Character range is null");
    if (range.length < 2) throw new IllegalArgumentException("Character range is less than 2 characters");
  }

  /**
   * Append a number of pseudo-random characters to the specified key.
   *
   * @param key    A current key
   * @param length The length of the key
   * @param range  The range of characters to pick from.
   */
  private static void append(StringBuilder key, String name, char[] range) {
    long seed = RANDOM.nextLong();
    try {
      MessageDigest md = MessageDigest.getInstance("SHA");
      byte[] bytes = (name+'-'+seed).getBytes(Charset.forName("UTF-8"));
      byte[] data = md.digest(bytes);
      for (byte a : data) {
        key.append(toChar(a, range));
      }
    } catch (NoSuchAlgorithmException ex) {
      // FIXME Potentially a serious issue
    }
  }

  /**
   * Append a number of pseudo-random characters to the specified key.
   *
   * @param key    A current key
   * @param length The length of the key
   * @param range The range of characters to pick from.
   */
  private static void append(StringBuilder key, int length, char[] range) {
    byte[] more = new byte[length];
    RANDOM.nextBytes(more);
    for (byte a : more) {
      key.append(toChar(a, range));
    }
  }

  /**
   * Return a character from the specified byte using the specified range of characters.
   *
   * @param b     The byte
   * @param range The range of characters to pick from.
   *
   * @return The corresponding character.
   */
  private static char toChar(byte b, char[] range) {
    return range[(b & 0x7f) % range.length];
  }

}
