package com.bertorl.client.p2p;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.bertorl.client.IOUtils.IOUtils;
import com.bertorl.client.services.ListenNodeServerConnectionService;

public class Peer implements Runnable {

	private static final String LOCALHOST = "localhost";
	private static final int PEER_CONNECTION_TO_SERVER_PORT = 24600;
	private String peerUUID;
	private ConcurrentHashMap<String, String> peerTable = new ConcurrentHashMap<String, String>();
	
	public Peer() throws IOException {
		this.peerUUID = IOUtils.getPeerUUID();
	}
	
	
	public void run() {
		
		ExecutorService executor = Executors.newSingleThreadExecutor();
		
		try {
			executor.execute(new ListenNodeServerConnectionService(peerTable));
			Socket peerConnectionToServer = new Socket(LOCALHOST, PEER_CONNECTION_TO_SERVER_PORT);
			PrintWriter out = new PrintWriter(new OutputStreamWriter(peerConnectionToServer.getOutputStream()));
			out.print(peerUUID);
			out.flush();
			out.close();
			peerConnectionToServer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
