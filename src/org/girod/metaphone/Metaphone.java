/*
MIT License

Copyright (c) 2014 Titus Wormer <tituswormer@gmail.com>
Copyright (c) 2018 Hervé Girod <herve.girod@gmail.com>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package org.girod.metaphone;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PropertyResourceBundle;
import java.util.regex.Pattern;

/**
 * A Metaphone class for English language.
 *
 * <h1>Usage</h1>
 * <pre>
 * Metaphone metaphone = new Metaphone();
 * String result = metaphone.metaphone("michael"); // should be "MXL"
 * </pre>
 *
 * @since 1.0
 */
public class Metaphone {
   private static Map<Pattern, String> substs = new HashMap<>();

   static {
      substs.put(Pattern.compile("[âãáàäÂÃÁÀÄ]"), "a");
      substs.put(Pattern.compile("[éèêëÉÈÊË]"), "e");
      substs.put(Pattern.compile("[íìîïÍÌÎÏ]"), "i");
      substs.put(Pattern.compile("[óòôõöÓÒÔÕÖ]"), "o");
      substs.put(Pattern.compile("[úùûüÚÙÛÜ]"), "u");
      substs.put(Pattern.compile("[ñÑ]"), "n");
      substs.put(Pattern.compile("[ç]"), "c");
      substs.put(Pattern.compile("[ç]"), "c");
      substs.put(Pattern.compile("[ß]"), "b");
   }
   private String phonized = "";
   private int index = 0;
   private int skip = 0;
   private String value = "";
   private final char sh = 'X';
   private final char th = '0';
   private boolean lc = false;
   private boolean accented = false;

   /**
    * Default Constructor. The result will be upper case.
    */
   public Metaphone() {
      this(false, false);
   }

   /**
    * Constructor.
    *
    * @param toLowerCase true if the result will be lower case
    */
   public Metaphone(boolean toLowerCase) {
      this.lc = toLowerCase;
   }

   /**
    * Constructor.
    *
    * @param toLowerCase true if the result will be lower case
    * @param accented true if the language has accented characters
    */
   public Metaphone(boolean toLowerCase, boolean accented) {
      this.lc = toLowerCase;
      this.accented = accented;
   }

   /**
    * Return true if the result will be lower case.
    *
    * @return true if the result will be lower case
    */
   public boolean isLowerCase() {
      return lc;
   }

   /**
    * Return true if the language has accented characters.
    *
    * @return true if the language has accented characters
    */
   public boolean isAccented() {
      return accented;
   }

   public static void main(String[] args) {
      URL url = Metaphone.class.getResource("metaphone.properties");
      try {
         PropertyResourceBundle prb = new PropertyResourceBundle(url.openStream());
         String version = prb.getString("version");
         String date = prb.getString("date");
         System.out.println("jMetaphone version " + version + " build on " + date);
         System.out.println("Distributed under the MIT License");
      } catch (IOException ex) {
         ex.printStackTrace();
      }
   }

   private String removeAccents(String value) {
      for (Entry<Pattern, String> subst : substs.entrySet()) {
         Pattern accents = subst.getKey();
         String noAccent = subst.getValue();
         value = accents.matcher(value).replaceAll(noAccent);
      }
      return value;
   }

   /**
    * Get the phonetics according to the original Metaphone algorithm from a value.
    *
    * @param value the value
    * @return the phonetics
    */
   public String metaphone(String value) {
      phonized = "";
      index = 0;
      skip = 0;
      if (accented) {
         this.value = removeAccents(value);
      } else {
         this.value = value;
      }

      // Find our first letter
      while (!Character.isLetter(current())) {
         if (current() == (char) -1) {
            return "";
         }
         index++;
      }

      switch (current()) {
         case 'A':
            // AE becomes E
            if (next() == 'E') {
               phonize('E');
               index += 2;
            } else {
               // Remember, preserve vowels at the beginning
               phonize('A');
               index++;
            }

            break; // [GKP]N becomes N
         case 'G':
         case 'K':
         case 'P':
            if (next() == 'N') {
               phonize('N');
               index += 2;
            }

            break; // WH becomes H, WR becomes R, W if followed by a vowel
         case 'W':
            if (next() == 'R') {
               phonize(next());
               index += 2;
            } else if (next() == 'H') {
               phonize(current());
               index += 2;
            } else if (vowel(next())) {
               phonize('W');
               index += 2;
            }
            // Ignore
            break; // X becomes S
         case 'X':
            phonize('S');
            index++;

            break; // Vowels are kept (we did A already)
         case 'E':
         case 'I':
         case 'O':
         case 'U':
            phonize(current());
            index++;
            break;
         default:
            break;
      }

      // On to the metaphoning
      while (current() != (char) -1) {
         // How many letters to skip because an eariler encoding handled multiple
         // letters
         skip = 1;

         // Ignore non-alphas
         if (!Character.isLetter(current()) || (current() == prev() && current() != 'C')) {
            index += skip;
            continue;
         }

         // eslint-disable-next-line default-case
         switch (current()) {
            // B -> B unless in MB
            case 'B':
               if (prev() != 'M') {
                  phonize('B');
               }

               break;
            // 'sh' if -CIA- or -CH, but not SCH, except SCHW (SCHW is handled in S)
            // S if -CI-, -CE- or -CY- dropped if -SCI-, SCE-, -SCY- (handed in S)
            // else K
            case 'C':
               if (soft(next())) {
                  // C[IEY]
                  if (next() == 'I' && at(2) == 'A') {
                     // CIA
                     phonize(sh);
                  } else if (prev() != 'S') {
                     phonize('S');
                  }
               } else if (next() == 'H') {
                  phonize(sh);
                  skip++;
               } else {
                  // C
                  phonize('K');
               }

               break; // J if in -DGE-, -DGI- or -DGY-, else T
            case 'D':
               if (next() == 'G' && soft(at(2))) {
                  phonize('J');
                  skip++;
               } else {
                  phonize('T');
               }

               break; // F if in -GH and not B--GH, D--GH, -H--GH, -H---GH
            // else dropped if -GNED, -GN,
            // else dropped if -DGE-, -DGI- or -DGY- (handled in D)
            // else J if in -GE-, -GI, -GY and not GG
            // else K
            case 'G':
               if (next() == 'H') {
                  if (!(noGhToF(at(-3)) || at(-4) == 'H')) {
                     phonize('F');
                     skip++;
                  }
               } else if (next() == 'N') {
                  if (!(!Character.isLetter(at(2)) || (at(2) == 'E' && at(3) == 'D'))) {
                     phonize('K');
                  }
               } else if (soft(next()) && prev() != 'G') {
                  phonize('J');
               } else {
                  phonize('K');
               }

               break; // H if before a vowel and not after C,G,P,S,T
            case 'H':
               if (vowel(next()) && !dipthongH(prev())) {
                  phonize('H');
               }

               break; // Dropped if after C, else K
            case 'K':
               if (prev() != 'C') {
                  phonize('K');
               }

               break; // F if before H, else P
            case 'P':
               if (next() == 'H') {
                  phonize('F');
               } else {
                  phonize('P');
               }

               break; // K
            case 'Q':
               phonize('K');
               break; // 'sh' in -SH-, -SIO- or -SIA- or -SCHW-, else S
            case 'S':
               if (next() == 'I' && (at(2) == 'O' || at(2) == 'A')) {
                  phonize(sh);
               } else if (next() == 'H') {
                  phonize(sh);
                  skip++;
               } else {
                  phonize('S');
               }

               break; // 'sh' in -TIA- or -TIO-, else 'th' before H, else T
            case 'T':
               if (next() == 'I' && (at(2) == 'O' || at(2) == 'A')) {
                  phonize(sh);
               } else if (next() == 'H') {
                  phonize(th);
                  skip++;
               } else if (!(next() == 'C' && at(2) == 'H')) {
                  phonize('T');
               }

               break; // F
            case 'V':
               phonize('F');
               break;
            case 'W':
               if (vowel(next())) {
                  phonize('W');
               }
               break; // KS
            case 'X':
               phonize("KS");
               break; // Y if followed by a vowel
            case 'Y':
               if (vowel(next())) {
                  phonize('Y');
               }

               break; // S
            case 'Z':
               phonize('S');
               break; // No transformation
            case 'F':
            case 'J':
            case 'L':
            case 'M':
            case 'N':
            case 'R':
               phonize(current());
               break;
         }

         index += skip;
      }

      if (lc) {
         return phonized.toLowerCase();
      } else {
         return phonized;
      }
   }

   private char current() {
      return at(0);
   }

   private char prev() {
      return at(-1);
   }

   private char next() {
      return at(1);
   }

   private void phonize(char c) {
      phonized += Character.toString(c);
   }

   private void phonize(String s) {
      phonized += s;
   }

   private char at(int offset) {
      if (index + offset >= value.length()) {
         return (char) -1;
      } else if (index + offset < 0) {
         return (char) 0;
      } else {
         return Character.toUpperCase(value.charAt(index + offset));
      }
   }

   private boolean vowel(char c) {
      return c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U';
   }

// Check whether c would make a `'C'` or `'G'` soft
   private boolean soft(char c) {
      return c == 'E' || c == 'I' || c == 'Y';
   }

// Check whether c would make `'GH'` an `'F'`
   private boolean noGhToF(char c) {
      return c == 'B' || c == 'D' || c == 'H';
   }

// Check whether c forms a dipthong when preceding H
   private boolean dipthongH(char c) {
      return c == 'C' || c == 'G' || c == 'P' || c == 'S' || c == 'T';
   }
}
