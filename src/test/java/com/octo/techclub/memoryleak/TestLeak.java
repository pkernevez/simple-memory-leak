package com.octo.techclub.memoryleak;

import static junit.framework.Assert.fail;
import static org.fest.assertions.Assertions.assertThat;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.WeakHashMap;

import org.junit.Test;

public class TestLeak {

	public static final int MAX_LOOP_TO_TRY = 1000;

	/** Volatile need to be members */
	volatile Throwable tException = null;

	@Test
	public void testLongRunningThreadWithThreadLocalAndALotOfClasses() {
		// Create a new Thread is required to be able to release memory at the
		// end of the test
		CreateObjectLoopThread longRunningThread = new CreateObjectLoopThread(true);
		longRunningThread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				e.printStackTrace();
			}
		});
		longRunningThread.setDaemon(false);
		longRunningThread.start();
		try {
			longRunningThread.join();
		} catch (InterruptedException e) {
			fail("Should not be interupted !");
		}
		// Duplicated usefull field from the release Thread
		boolean hasOutOfMemoryException = longRunningThread.hasOutOfMemoryException;
		Throwable exception = longRunningThread.unexpectedException;
		longRunningThread = null; // Free the memory
		assertThat(exception).isNull();
		assertThat(hasOutOfMemoryException).isTrue();
	}

	@Test
	public void testAddObjectIntoCollectionCreateOOM() throws Exception {
		try {
			Set<Object> tSet = new HashSet<>();
			for (int i = 0; i < MAX_LOOP_TO_TRY; i++) {
				tSet.add(new SimpleClass());
			}
			fail("We should have an Out of Memory Error !");
		} catch (OutOfMemoryError e) {
			// Nothing to do the collection would garbage collected.
		}
	}

	@Test
	public void testAddObjectIntoWeakCollectionDoesntCreateOOM() {
		Set<Object> tSet = Collections.newSetFromMap(new WeakHashMap<Object, Boolean>());
		for (int i = 0; i < MAX_LOOP_TO_TRY; i++) {
			tSet.add(new SimpleClass());
		}
		// Check that objects has been released during the test
		assertThat(tSet.size()).isLessThan(MAX_LOOP_TO_TRY);
	}

}
