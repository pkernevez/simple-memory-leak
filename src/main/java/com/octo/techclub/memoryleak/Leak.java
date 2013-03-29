package com.octo.techclub.memoryleak;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Leak {

    public static final int BUFFER_SIZE = 1000000;

    public static class LeakLoader extends ClassLoader {
        private byte[] leakCode;

        public LeakLoader() throws IOException {
            ByteArrayOutputStream tStream = new ByteArrayOutputStream();
            InputStream tInput = getResourceAsStream("com/octo/techclub/memoryleak/LeakCreator.class");
            byte[] buffer = new byte[1000];
            for (int readBytes = tInput.read(buffer); readBytes > 0; readBytes = tInput.read(buffer)) {
                tStream.write(buffer, 0, readBytes);
            }
            leakCode = tStream.toByteArray();
        }

        public Class<?> myLoadClass() throws ClassNotFoundException {
            Class<?> tClass = defineClass("com.octo.techclub.memoryleak.LeakCreator", leakCode, 0, leakCode.length);
            resolveClass(tClass);
            return tClass;
        }

    }

//	The application creates a long-running thread (or use a thread pool to leak even faster).
//	The thread loads a class via an (optionally custom) ClassLoader.
//	The class allocates a large chunk of memory (e.g. new byte[1000000]), stores a strong reference to it in a static field, and then stores a reference to itself in a ThreadLocal. Allocating the extra memory is optional (leaking the Class instance is enough), but it will make the leak work that much faster.
//	The thread clears all references to the custom class or the ClassLoader it was loaded from.
//	Repeat.

    public static void main(String[] args) {

        Thread listener = new Thread() {
            @Override
            public void run() {
                try (ServerSocket serverSocket = new ServerSocket(1111)) {
                    for (;;) {
                        try (Socket clientSocket = serverSocket.accept()) {
                            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                            String s;
                            StringBuilder sb = new StringBuilder();
                            while ((s = in.readLine()) != null) {
                                if (s.isEmpty()) {
                                    break;
                                }
                                sb.append(s);
                            }

                            System.out.println(sb);
                            out.write(sb.toString());
                            try {
                                Class<?> c = new LeakLoader().myLoadClass();
                                Object o = c.newInstance();
                            } catch (Exception e) {
                                new RuntimeException(e);
                            }

                            out.close();
                            in.close();
                        }
                    }
                } catch (IOException e) {
                    new RuntimeException(e);
                }
            }
        };
        listener.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                e.printStackTrace();
            }
        });
        listener.setDaemon(false);
        listener.start();
    }

}
