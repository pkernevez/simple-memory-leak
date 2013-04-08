package com.octo.techclub.memoryleak;

public class LeakCreator {
	public final static byte[] leak = new byte[Leak.BUFFER_SIZE];

	public final static ThreadLocal<Object> local = new ThreadLocal<Object>();
	/** Use boolean and not an enumeration due to classloaader issue.*/ 
	public LeakCreator(boolean referenceObject) {
		if (referenceObject) {
			local.set(this);
//			System.out.println("new instance with object");
		} else {
			local.set(LeakCreator.class);
//			System.out.println("new instance with class");
		}
	}
}
