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
package org.metaphone;

import static org.junit.Assert.assertEquals;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @since 1.0
 */
public class MetaphoneTest {

   public MetaphoneTest() {
   }

   @BeforeClass
   public static void setUpClass() {
   }

   @AfterClass
   public static void tearDownClass() {
   }

   @Before
   public void setUp() {
   }

   @After
   public void tearDown() {
   }

   /**
    * Test of metaphone method.
    */
   @Test
   public void testMetaphoneSpaces() {
      System.out.println("MetaphoneTest : testMetaphoneSpaces");
      Metaphone metaphone = new Metaphone();
      String result = metaphone.metaphone(" f o ");
      assertEquals("Result", "F", result);
   }

   /**
    * Test of metaphone method.
    */
   @Test
   public void testMetaphoneDigits() {
      System.out.println("MetaphoneTest : testMetaphoneDigits");
      Metaphone metaphone = new Metaphone();
      String result = metaphone.metaphone("0f1o2");
      assertEquals("Result", "F", result);
   }

   /**
    * Test of metaphone method.
    */
   @Test
   public void testMetaphoneNoLetters() {
      System.out.println("MetaphoneTest : testMetaphoneNoLetters");
      Metaphone metaphone = new Metaphone();
      String result = metaphone.metaphone("0 1 2");
      assertEquals("Result", "", result);
   }

   /**
    * Test of metaphone method.
    */
   @Test
   public void testMetaphoneAdjacentLetters() {
      System.out.println("MetaphoneTest : testMetaphoneAdjacentLetters");
      Metaphone metaphone = new Metaphone();
      String result = metaphone.metaphone("Agrippa");
      assertEquals("Result", "AKRP", result);
   }

   /**
    * Test of metaphone method.
    */
   @Test
   public void testMetaphoneInitialLetters() {
      System.out.println("MetaphoneTest : testMetaphoneInitialLetters");
      Metaphone metaphone = new Metaphone();

      String result = metaphone.metaphone("oo");
      assertEquals("Result", "O", result);

      result = metaphone.metaphone("ee");
      assertEquals("Result", "E", result);
   }

   /**
    * Test of metaphone method.
    */
   @Test
   public void testMetaphoneVarious() {
      System.out.println("MetaphoneTest : testMetaphoneVarious");
      Metaphone metaphone = new Metaphone();

      String result = metaphone.metaphone("abandonware");
      assertEquals("Result", "ABNTNWR", result);

      result = metaphone.metaphone("hiccups");
      assertEquals("Result", "HKKPS", result);

      result = metaphone.metaphone("pneumatics");
      assertEquals("Result", "NMTKS", result);

      result = metaphone.metaphone("aerial");
      assertEquals("Result", "ERL", result);
   }
}
