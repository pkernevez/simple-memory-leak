package net.kernevez.leak;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Leak {

	private static final int MAX = 10000;
	public static final int BUFFER_SIZE = 1000000;

	private static ThreadLocal<Object> ref = new ThreadLocal<Object>();

	public static class LeakLoader extends ClassLoader {
		private byte[] leakCode;

		public LeakLoader() throws IOException {
			ByteArrayOutputStream tStream = new ByteArrayOutputStream();
			InputStream tInput = getResourceAsStream("net/kernevez/leak/LeakCreator.class");
			byte[] buffer = new byte[1000];
			for (int readBytes = tInput.read(buffer); readBytes > 0; readBytes = tInput.read(buffer)) {
				tStream.write(buffer, 0, readBytes);
			}
			leakCode = tStream.toByteArray();
		}

		public Class<?> myLoadClass() throws ClassNotFoundException{
			Class<?> tClass = defineClass("net.kernevez.leak.LeakCreator", leakCode, 0, leakCode.length);
			resolveClass(tClass);
			return tClass;
		}

	}

	public static void main(String[] args) {
		try {
			for (int i = 0; i < MAX; i++) {
				Runnable tRun = new Runnable() {

					@Override
					public void run() {
						try {
							Class<?> tClass = new LeakLoader().myLoadClass();
							ref.set(tClass.newInstance());
						} catch (Exception e) {
							e.printStackTrace();
							System.out.println("Error IN THREAD: " + e);
						}
					}
				};
				Thread tThread = new Thread(tRun);
				tThread.setDaemon(false);
				tThread.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error : " + e);
		}
	}

}
