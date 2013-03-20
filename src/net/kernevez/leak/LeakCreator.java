package net.kernevez.leak;


public class LeakCreator {
	public static byte[] leak = new byte[Leak.BUFFER_SIZE];
	public static ThreadLocal<Class<LeakCreator>> local = new ThreadLocal<Class<LeakCreator>>();
	static {
		//System.out.println("Load class...");
		local.set(LeakCreator.class);
	}
	
	public LeakCreator () {
	}
}
