# jmetaphone
A Java port of the https://github.com/words/metaphone Javascript repository

There are two options for the Metaphone result:
* Uppercase (the default), or Lowercase
* Without processing accented characters (the default), or with processing accented characters

Use:

```java
Metaphone metaphone = new Metaphone();
metaphone.metaphone("michael") // => "MXL"
metaphone.metaphone("crevalle") // => "KRFL"
metaphone.metaphone("Filipowitz") // => "FLPWTS"
metaphone.metaphone("Xavier") // => "SFR"
metaphonemetaphone.metaphone("delicious") // => "TLSS"
metaphone("acceptingness") // => "AKSPTNKNS'
metaphone.metaphone("allegrettos") // => "ALKRTS"