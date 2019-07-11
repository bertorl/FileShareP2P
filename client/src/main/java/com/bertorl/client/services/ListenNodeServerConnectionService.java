package com.bertorl.client.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.bertorl.client.IOUtils.IOUtils;

public class ListenNodeServerConnectionService implements Runnable {

	private static final int SERVICE_CONNECTION_PORT = 24601;
	private ConcurrentHashMap<String, String> peerTable;

	public ListenNodeServerConnectionService() {
	}

	public ListenNodeServerConnectionService(ConcurrentHashMap<String, String> peerTable) {
		super();
		this.peerTable = peerTable;
	}

	@Override
	public void run() {
		ServerSocket peerListen = null;
		try {
			ExecutorService executor = Executors.newSingleThreadExecutor();
			peerListen = new ServerSocket(SERVICE_CONNECTION_PORT);	
			boolean var = true;
			while (var) {
				Socket sock = peerListen.accept();
				executor.execute(() -> {
					try {
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(sock.getInputStream()));
						String line="";
						while ((line = reader.readLine()) != null) {
							StringTokenizer st = new StringTokenizer(line, " ");
							String UUID = st.nextToken();
							String peerIP = st.nextToken();
							peerTable.put(UUID, peerIP);
						}
						reader.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
				IOUtils.logTableToTextFile(peerTable);
			}
			peerListen.close();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
