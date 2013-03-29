package com.octo.techclub.memoryleak;

public class SimpleClass {
	/** Allocate memory to consume heap quickly */
	public byte[] leak = new byte[Leak.BUFFER_SIZE];

}
