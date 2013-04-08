simple-memory-leak
==================

This project has been made to illustrate example of memory leaks in Java.

Run the tests [TestLeak](https://github.com/pkernevez/simple-memory-leak/tree/master/src/main/java/com/octo/techclub/memoryleak/Leak.java "TestLeak")

# List of tests
## Adding indefinitely objects into a collection
[TestLeak](https://github.com/pkernevez/simple-memory-leak/tree/master/src/main/java/com/octo/techclub/memoryleak/Leak.java
 "See testAddObjectIntoCollectionCreateOOM")
The more simple test : just go in an infinite loop that add object to a collection.
This is what most people have in mind when they speak about 'memory leak'. It's more a bug
than a leak : the memory is still accessible, you are just using more memory than your JVM aload.

## Adding indefinitely objects into a weak collection doesn't create leak
[TestLeak](https://github.com/pkernevez/simple-memory-leak/tree/master/src/main/java/com/octo/techclub/memoryleak/Leak.java
 "See testAddObjectIntoWeakCollectionDoesntCreateOOM")
 Just to illustrate the usage of weak collections that release memory if needed. Not a leak.
 
## 
//  The application creates a long-running thread (or use a thread pool to leak even faster).
//  The thread loads a class via an (optionally custom) ClassLoader.
//  The class allocates a large chunk of memory (e.g. new byte[1000000]), stores a strong reference
 to it in a static field, and then stores a reference to itself in a ThreadLocal. 
 Allocating the extra memory is optional (leaking the Class instance is enough), 
 but it will make the leak work that much faster.
//  The thread clears all references to the custom class or the ClassLoader it was loaded from.
//  Repeat. 
