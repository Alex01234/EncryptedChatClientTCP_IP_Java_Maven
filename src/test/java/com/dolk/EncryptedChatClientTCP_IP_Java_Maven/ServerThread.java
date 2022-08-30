package com.dolk.EncryptedChatClientTCP_IP_Java_Maven;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerThread extends Thread{
	ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;
	private Server mainServer;
	
	public ServerThread(Socket serverThreadSocket, Server mainServer) throws IOException {
		this.outputStream = new ObjectOutputStream(serverThreadSocket.getOutputStream());
		this.inputStream = new ObjectInputStream(serverThreadSocket.getInputStream());
		this.mainServer = mainServer;
	}
	
	@Override
	public void run() {
		try {
			while(true) {
				Object object = inputStream.readObject();
				mainServer.distributeObjectToAllServerThreads(object);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
