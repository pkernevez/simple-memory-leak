package com.octo.techclub.memoryleak;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import static junit.framework.Assert.*;
import static org.fest.assertions.Assertions.*;

public class TestLeak {

	public static final int MAX_LOOP_TO_TRY = 1000;

	@Test
	public void testLongRunningThreadWithThreadLocalAndALotOfClasses(){
		
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
	public void testAddObjectIntoCollectionAndClearItOftenDoesntCreateOOM() {
			Set<Object> tSet = new HashSet<>();
			for (int i = 0; i < MAX_LOOP_TO_TRY; i++) {
				tSet.add(new SimpleClass());
				if (i%100==0) tSet.clear();
			}
	}

}
