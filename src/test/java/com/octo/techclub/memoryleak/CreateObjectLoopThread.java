package com.octo.techclub.memoryleak;

import static junit.framework.Assert.fail;

import com.octo.techclub.memoryleak.Leak.LeakLoader;

public class CreateObjectLoopThread extends Thread {
	private boolean referenceObject;
	public volatile boolean hasOutOfMemoryException = false;
	public volatile Throwable unexpectedException;

	public CreateObjectLoopThread(boolean pReferenceObject) {
		referenceObject = pReferenceObject;
	}

	@Override
	public void run() {
		try {
			for (int i = 0; i < TestLeak.MAX_LOOP_TO_TRY; i++) {
				Class<?> c = new LeakLoader().myLoadClass();
				c.getConstructor(Boolean.TYPE).newInstance(referenceObject);
			}
			fail("We should have an Out of Memory Error !");
		} catch (OutOfMemoryError e) {
			// Nothing to do the collection would garbage collected when
			// we stop referencing the thread
			System.out.println("YYYYEEEESSSS !");
			hasOutOfMemoryException = true;
		} catch (Exception e) {
			unexpectedException = e;
			throw new RuntimeException(e);
		}
	}

}
