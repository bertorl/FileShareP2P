package com.bertorl.server.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

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
			peerIP = socket.getInetAddress().getHostAddress();
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
				out.println();
				out.flush();
				out.close();
				peerSocket.close();
			}

			else {
				peerTable.put(peerUUID, peerIP);
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
						out.println();
						out.flush();
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
