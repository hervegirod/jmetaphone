/*
MIT License

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
public class Metaphone2Test {

   public Metaphone2Test() {
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
   public void testMetaphoneVarious() {
      System.out.println("Metaphone2Test : testMetaphoneVarious");
      Metaphone metaphone = new Metaphone(false, true);

      String result = metaphone.metaphone("michael");
      assertEquals("Result", "MXL", result);

      result = metaphone.metaphone("crevalle");
      assertEquals("Result", "KRFL", result);

      result = metaphone.metaphone("Filipowitz");
      assertEquals("Result", "FLPWTS", result);

      result = metaphone.metaphone("Xavier");
      assertEquals("Result", "SFR", result);

      result = metaphone.metaphone("delicious");
      assertEquals("Result", "TLSS", result);

      result = metaphone.metaphone("acceptingness");
      assertEquals("Result", "AKSPTNKNS", result);

      result = metaphone.metaphone("allegrettos");
      assertEquals("Result", "ALKRTS", result);
   }

   /**
    * Test of metaphone method.
    */
   @Test
   public void testMetaphoneAccented() {
      System.out.println("Metaphone2Test : testMetaphoneAccented");
      Metaphone metaphone = new Metaphone(false, true);

      String result = metaphone.metaphone("là");
      assertEquals("Result", "L", result);

      result = metaphone.metaphone("Hervé");
      assertEquals("Result", "HRF", result);

      result = metaphone.metaphone("Lucélia");
      assertEquals("Result", "LSL", result);
   }
}
