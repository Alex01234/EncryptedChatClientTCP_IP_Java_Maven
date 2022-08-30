package com.dolk.EncryptedChatClientTCP_IP_Java_Maven;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.atomic.AtomicBoolean;

public class ServerSocketMock implements Runnable {
	private Thread t;
	private final AtomicBoolean running = new AtomicBoolean(false);
	ServerSocket serverSocket = null;
	
	public void start(int port) {
        t = new Thread(this);
        try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
        t.start();
    }
 
    public void stop() {
        running.set(false);
    }
    
    public void run() {
    	running.set(true);
    	while(running.get()) {
    		try {
				serverSocket.accept();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }
}//class ServerSocketMock
