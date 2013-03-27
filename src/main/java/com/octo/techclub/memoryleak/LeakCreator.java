package com.octo.techclub.memoryleak;


public class LeakCreator {
	public static byte[] leak = new byte[Leak.BUFFER_SIZE];

    public static ThreadLocal<Object> local = new ThreadLocal<Object>();
	static {
        local.set(LeakCreator.class);
	}
}
