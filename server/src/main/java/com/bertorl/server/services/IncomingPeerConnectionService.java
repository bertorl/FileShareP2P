package com.bertorl.server.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import com.bertorl.server.IOUtils.IOUtils;

//TODO APLICAR PATRÓN SINGLETON AL SERVICIO 
public class IncomingPeerConnectionService implements Runnable {

	private static final int SERVICE_CONNECTION_PORT = 24601;
	private Socket socket;
	private ConcurrentHashMap<String, String> peerTable;

	public IncomingPeerConnectionService(Socket socket, ConcurrentHashMap<String, String> peerTable) {
		this.socket = socket;
		this.peerTable = peerTable;
	}

	@Override
	public void run() {
		String peerUUID = "";
		// String line = "";
		String peerIP;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			peerUUID = reader.readLine();
			peerIP = 
					socket.getInetAddress().getHostAddress();
			System.out.println(peerIP);
			reader.close();
			socket.close();

			if (peerTable.containsKey(peerUUID) && peerTable.get(peerUUID).equals(peerIP)) {
				Socket peerSocket = new Socket(peerIP, SERVICE_CONNECTION_PORT);
				PrintWriter out = new PrintWriter(new OutputStreamWriter(peerSocket.getOutputStream()));
				peerTable.forEach((k, v) -> {
					try {
						out.println(k + " " + v);
						out.flush();
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
				out.close();
				peerSocket.close();
				IOUtils.logTableToTextFile(peerTable);
			}

			else {
				peerTable.put(peerUUID+"a", peerIP);	
				peerTable.forEach((k, v) -> {
					try {
						Socket multiPeerSocket = new Socket(v, SERVICE_CONNECTION_PORT);
						PrintWriter out = new PrintWriter(new OutputStreamWriter(multiPeerSocket.getOutputStream()));
						peerTable.forEach((key, value) -> {
							try {
								out.println(key + " " + value);
								out.flush();
							} catch (Exception e) {
								e.printStackTrace();
							}
						});
						out.close();
						multiPeerSocket.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
