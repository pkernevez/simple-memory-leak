simple-memory-leak
==================

This project has been made for illustrating exemple of memory leak in Java.

Run the tests [TestLeak](https://github.com/pkernevez/simple-memory-leak/tree/master/src/main/java/com/octo/techclub/memoryleak/Leak.java "TestLeak")

# List of tests
## Simple infinitely adding object into a collection
[TestLeak](https://github.com/pkernevez/simple-memory-leak/tree/master/src/main/java/com/octo/techclub/memoryleak/Leak.java
 "See testAddObjectIntoCollectionCreateOOM")
The more simple test : just go in an infinite loop that add object to a collection.
This is what most people have in mind when they speak about 'memory leak'. It's more a bug
than a leak : the memory is still accessible, you are just using more memory than your JVM aload.

