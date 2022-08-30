package com.dolk.EncryptedChatClientTCP_IP_Java_Maven;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class Server {
	
	ArrayList<ServerThread> serverThreads;
	Iterator<ServerThread> serverThreadsIterator;
	
	private void runServer (){
		serverThreads = new ArrayList<>();
		
      try {
    	try (ServerSocket mainServerSocket = new ServerSocket(4848)) {
			while(true) {
				Socket serverThreadSocket = mainServerSocket.accept();
				ServerThread serverThread = new ServerThread(serverThreadSocket, this);
				serverThreads.add(serverThread);
				serverThread.start();
			}
		}
    } catch (Exception ex) {
    	ex.printStackTrace();
    }
	}
	
	void distributeObjectToAllServerThreads(Object object) {
		serverThreadsIterator = serverThreads.iterator();
		ServerThread serverThread;
		while(serverThreadsIterator.hasNext()) {
			try {
				serverThread = (ServerThread) serverThreadsIterator.next();
				serverThread.outputStream.writeObject(object);
			} catch (Exception ex) {
				serverThreadsIterator.remove();
				ex.printStackTrace();
			}
		}
	}
	
	public static void main () {
		Server server = new Server();
		server.runServer();
	}
}
